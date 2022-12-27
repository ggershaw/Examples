package org.gershaw.quickfixj.springboot.client.service;

import io.allune.quickfixj.spring.boot.starter.model.Logon;
import io.allune.quickfixj.spring.boot.starter.template.QuickFixJTemplate;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.gershaw.quickfixj.springboot.client.component.MarketDataRequestor;
import org.gershaw.quickfixj.springboot.util.Utils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import quickfix.Initiator;
import quickfix.SessionID;
import quickfix.field.MDEntryType;
import quickfix.fix50sp2.MarketDataRequest;

import java.util.Arrays;
import java.util.Map;

@ConditionalOnProperty(prefix = "quickfixj.client.requester", name = "request", havingValue = "true")
@Service
@Slf4j
public class MarketDataRequestService {
    private final QuickFixJTemplate fixJTemplate;
    private final Character[] mdEntryTypes;
    private final Map<String, String> instruments;
    private final Initiator initiator;

    public MarketDataRequestService(final QuickFixJTemplate fixJTemplate,
                                    @Value("${quickfixj.client.requester.mdentry}") final Character[] mdEntryTypes,
                                    @Value("#{${quickfixj.client.requester.instruments}}") final Map<String, String> instruments,
                                    final Initiator initiator) {
        this.fixJTemplate = fixJTemplate;
        this.mdEntryTypes = Arrays.copyOf(mdEntryTypes, mdEntryTypes.length);
        this.instruments = Map.copyOf(instruments);
        this.initiator = initiator;
    }

    @EventListener
    public void handleOnLogon(final Logon logon) {
        log.info("Received: {}", logon);
        final MarketDataRequest marketDataRequest = MarketDataRequestor.of(mdEntryTypes, instruments);
        final Map<SessionID, Boolean>  failures = Utils.send(fixJTemplate, initiator, marketDataRequest);
        if(!failures.isEmpty()) {
            log.warn("Failed to send to {}", failures.keySet());
        }
    }

}
