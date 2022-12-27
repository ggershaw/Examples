package org.gershaw.quickfixj.springboot.util;

import io.allune.quickfixj.spring.boot.starter.template.QuickFixJTemplate;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import quickfix.Connector;
import quickfix.Initiator;
import quickfix.Message;
import quickfix.SessionID;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor
public class Utils {

    public static void printMessage() {
        log.info("");
        log.info(
            "*************************************************************************************");
        log.info(
            "*************************************************************************************");
        log.info(
            "*****************************Press <enter> twice to quit*****************************");
        log.info(
            "*************************************************************************************");
        log.info(
            "*************************************************************************************");
        log.info("");
    }

    public static Map<SessionID, Boolean> send(@NonNull final QuickFixJTemplate fixJTemplate,
                                               @NonNull final Connector connector,
                                               @NonNull final Message message) {
        final Map<SessionID, Boolean> results = new HashMap<>();
        connector.getSessions()
            .forEach(s -> results.put(s, fixJTemplate.send(message, s)));

        return results.entrySet().stream()
            .filter(e -> e.getValue() ==Boolean.FALSE)
            .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
