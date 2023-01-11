package org.gershaw.quickfixj.springboot.client.service;

import io.allune.quickfixj.spring.boot.starter.template.QuickFixJTemplate;
import lombok.extern.slf4j.Slf4j;
import org.gershaw.quickfixj.springboot.client.component.MarketDataRequestor;
import org.gershaw.quickfixj.springboot.util.Utils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import quickfix.Application;
import quickfix.DoNotSend;
import quickfix.FieldNotFound;
import quickfix.IncorrectDataFormat;
import quickfix.IncorrectTagValue;
import quickfix.Initiator;
import quickfix.Message;
import quickfix.RejectLogon;
import quickfix.SessionID;
import quickfix.UnsupportedMessageType;
import quickfix.fix50sp2.MarketDataRequest;

import java.util.Arrays;
import java.util.Map;

@ConditionalOnProperty(prefix = "quickfixj.client.requester", name = "request", havingValue = "true")
@Service("clientApplication")
@Slf4j
public class MarketDataRequestService implements Application {
    private final QuickFixJTemplate fixJTemplate;
    private final Character[] mdEntryTypes;
    private final Map<String, String> instruments;
    private final Initiator initiator;

    public MarketDataRequestService(final QuickFixJTemplate fixJTemplate,
                                    @Value("${quickfixj.client.requester.mdentry}") final Character[] mdEntryTypes,
                                    @Value("#{${quickfixj.client.requester.instruments}}") final Map<String, String> instruments,
                                    @Lazy final Initiator initiator) {
        this.fixJTemplate = fixJTemplate;
        this.mdEntryTypes = Arrays.copyOf(mdEntryTypes, mdEntryTypes.length);
        this.instruments = Map.copyOf(instruments);
        this.initiator = initiator;
    }

    @Override
    public void onCreate(SessionID sessionId) {
        log.info("onCreate: {}", sessionId);
    }

    @Override
    public void onLogon(SessionID sessionId) {
        log.info("Received Logon: {}", sessionId);
        final MarketDataRequest marketDataRequest = MarketDataRequestor.of(mdEntryTypes, instruments);
        final Map<SessionID, Boolean>  failures = Utils.send(fixJTemplate, initiator, marketDataRequest);
        if(!failures.isEmpty()) {
            log.warn("Failed to send to {}", failures.keySet());
        }
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
    }
}
