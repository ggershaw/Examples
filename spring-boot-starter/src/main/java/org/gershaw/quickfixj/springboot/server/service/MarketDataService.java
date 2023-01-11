package org.gershaw.quickfixj.springboot.server.service;

import io.allune.quickfixj.spring.boot.starter.template.QuickFixJTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.gershaw.quickfixj.springboot.server.component.MarketDataIncrementalRefreshFactory;
import org.gershaw.quickfixj.springboot.server.component.MarketDataIncrementalRefreshFactory.Security;
import org.gershaw.quickfixj.springboot.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import quickfix.Acceptor;
import quickfix.Application;
import quickfix.DoNotSend;
import quickfix.FieldNotFound;
import quickfix.IncorrectDataFormat;
import quickfix.IncorrectTagValue;
import quickfix.Message;
import quickfix.RejectLogon;
import quickfix.SessionID;
import quickfix.UnsupportedMessageType;
import quickfix.field.MsgSeqNum;
import quickfix.field.MsgType;
import quickfix.fix50sp2.MarketDataIncrementalRefresh;
import quickfix.fix50sp2.MarketDataRequest;
import quickfix.fix50sp2.component.InstrmtMDReqGrp;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@ConditionalOnProperty(prefix = "quickfixj.server.publisher", name = "publish", havingValue = "true")
@Service("serverApplication")
@Slf4j
public class MarketDataService implements Application {

  private final QuickFixJTemplate quickFixJTemplate;
  private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
  private final Acceptor acceptor;
  private final long period;
  private final TimeUnit timeUnit;
  private ScheduledFuture<?> scheduledFuture;

  public MarketDataService(final QuickFixJTemplate quickFixJTemplate,
      @Lazy final Acceptor acceptor,
      @Value("${quickfixj.server.publisher.schedule.period}") final long period,
      @Value("${quickfixj.server.publisher.schedule.time-unit}") final TimeUnit timeUnit) {
    this.quickFixJTemplate = quickFixJTemplate;
    this.acceptor = acceptor;
    this.period = period;
    this.timeUnit = timeUnit;
  }

  /**
   * stops the internal ScheduledExecutor
   * @return true if there were no tasks left to be processed
   */
  public boolean stop(){
    List<Runnable> runnables = executorService.shutdownNow();
    return runnables.isEmpty();
  }

  private void schedule(MarketDataRequest marketDataRequest) {
    if (null != scheduledFuture) {
      scheduledFuture.cancel(false);
    }
    scheduledFuture = executorService.scheduleAtFixedRate(
        new InnerRunnable(marketDataRequest, acceptor), 0, period, timeUnit);
  }

  private static String getMsgType(Message message) {
    final String msgType;
    try {
      msgType = message.getHeader().getField(new MsgType()).getValue();
    } catch (FieldNotFound e) {
      try {
        log.error("Malformed Message with seq no {}, missing msgTyoe",
            message.getHeader().getField(new MsgSeqNum()).getValue());
        throw new RejectedExecutionException(e);
      } catch (FieldNotFound ex) {
        throw new RejectedExecutionException(ex);
      }
    }
    return msgType;
  }

  @Override
  public void onCreate(SessionID sessionId) {
    log.info("onCreate: {}", sessionId);
  }

  @Override
  public void onLogon(SessionID sessionId) {
    log.info("onLogon: {}", sessionId);
  }

  @Override
  public void onLogout(SessionID sessionId) {
    log.info("onLogout: {}", sessionId);
  }

  @Override
  public void toAdmin(Message message, SessionID sessionId) {
    log.info("toAdmin: {}", sessionId);
  }

  @Override
  public void fromAdmin(Message message, SessionID sessionId) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {
    log.info("fromAdmin: {}", sessionId);
  }

  @Override
  public void toApp(Message message, SessionID sessionId) throws DoNotSend {
    log.info("toApp: {}", sessionId);
  }

  @Override
  public void fromApp(Message message, SessionID sessionId) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
    log.info("fromApp: {}", sessionId);
    final String msgType = getMsgType(message);

    if (msgType.equals(MsgType.MARKET_DATA_REQUEST)) {
      final MarketDataRequest marketDataRequest = (MarketDataRequest) message;
      schedule(marketDataRequest);
    }
  }

  @RequiredArgsConstructor
  private class InnerRunnable implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(InnerRunnable.class);
    final MarketDataRequest marketDataRequest;
    final Acceptor acceptor;

    @Override
    public void run() {
      final String mdReqId = extractMdReqId(marketDataRequest);
      final Set<Security> securities = extractSecuritiesFromMsg(marketDataRequest);
      final Set<MarketDataIncrementalRefresh> mdirs = createMarketDataIncrementalRefresh(securities, mdReqId);
      mdirs.stream()
          .map(m -> Utils.send(quickFixJTemplate, acceptor, m))
          .forEach(this::checkForFailure);
    }

    private static Set<Security> extractSecuritiesFromGroup(final InstrmtMDReqGrp instrmtMDReqGrp) {
      try {
        final int repeatingGroupSize = instrmtMDReqGrp.getNoRelatedSym().getValue();
        final Set<Security> securities = new HashSet<>(repeatingGroupSize);

        for (int i = 1; i <= repeatingGroupSize; i++) {
          final MarketDataRequest.NoRelatedSym relatedSym = new MarketDataRequest.NoRelatedSym();
          instrmtMDReqGrp.getGroup(i, relatedSym);
          final Security security = new Security(relatedSym.getSecurityID().getValue(),
              relatedSym.getSecurityIDSource().getValue());
          securities.add(security);
        }
        return securities;
      } catch (FieldNotFound e) {
        log.error("Invalid Msg. InstrmtMDReqGrp ia missing a field", e);
        return Set.of();
      }
    }

    private static Set<Security> extractSecuritiesFromMsg(final MarketDataRequest marketDataRequest) {
      final String mdReqId = extractMdReqId(marketDataRequest);
      if (mdReqId.isEmpty()) {
        return Set.of();
      }

      final Set<Security> securities = new HashSet<>();
      try {
        final InstrmtMDReqGrp instrmtMDReqGrp = new InstrmtMDReqGrp();
        marketDataRequest.get(instrmtMDReqGrp);
        securities.addAll(extractSecuritiesFromGroup(instrmtMDReqGrp));

        logger.debug("Extracted  securities: {} from request id: {}", securities, mdReqId);
        return securities;
      } catch (FieldNotFound e) {
        log.error("Invalid Msg. MarketDataRequest id={} is missing a necessary field.",
            mdReqId, e);
        return Set.of();
      }
    }

    private static String extractMdReqId(final MarketDataRequest marketDataRequest) {
      try {
        return marketDataRequest.getMDReqID().getValue();
      } catch (FieldNotFound e) {
        log.error("Invalid msg. Missing MDReqID. Message :{}", marketDataRequest);
        return Strings.EMPTY;
      }
    }

    private static Set<MarketDataIncrementalRefresh> createMarketDataIncrementalRefresh(
        final Set<Security> securities,
        final String mdReqId) {
      return securities.stream()
          .map(s -> MarketDataIncrementalRefreshFactory.of(mdReqId, s))
          .collect(Collectors.toUnmodifiableSet());
    }

    private void checkForFailure(final Map<SessionID, Boolean> failures) {
      if (!CollectionUtils.isEmpty(failures)) {
        log.error("Failure to send to {}", failures);
      }
    }
  }
}
