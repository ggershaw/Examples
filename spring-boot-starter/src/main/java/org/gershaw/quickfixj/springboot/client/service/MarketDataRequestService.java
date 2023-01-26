package org.gershaw.quickfixj.springboot.client.service;

import io.allune.quickfixj.spring.boot.starter.template.QuickFixJTemplate;
import java.util.Arrays;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.gershaw.quickfixj.springboot.client.component.MarketDataRequestor;
import org.gershaw.quickfixj.springboot.util.Utils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import quickfix.ApplicationAdapter;
import quickfix.FieldNotFound;
import quickfix.Initiator;
import quickfix.Message;
import quickfix.SessionID;
import quickfix.field.MsgType;
import quickfix.fix50sp2.MarketDataIncrementalRefresh;
import quickfix.fix50sp2.MarketDataRequest;

@ConditionalOnProperty(prefix = "quickfixj.client.requester", name = "request", havingValue = "true")
@Service("clientApplication")
@Slf4j
@SuppressWarnings("unused")
public class MarketDataRequestService extends ApplicationAdapter {
    private final QuickFixJTemplate fixJTemplate;
    private final Character[] mdEntryTypes;
    private final Map<String, String> instruments;
    private final Initiator initiator;

    @SuppressWarnings("unused")
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
    public void onLogon(SessionID sessionId) {
        log.info("Received Logon: {}", sessionId);
        final MarketDataRequest marketDataRequest = MarketDataRequestor.of(mdEntryTypes, instruments);
        final Map<SessionID, Boolean>  failures = Utils.send(fixJTemplate, initiator, marketDataRequest);
        if(!failures.isEmpty()) {
            log.warn("Failed to send to {}", failures.keySet());
        }
    }


    @Override
    public void fromApp(Message message, SessionID sessionId) throws FieldNotFound{
        String msgType = message.getHeader().getField(new MsgType()).getValue();
        if(msgType.equals(MarketDataIncrementalRefresh.MSGTYPE)){
            log.info("Received MD: {}", message);
        }
    }
}
