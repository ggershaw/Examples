package org.gershaw.quickfixj.ssl.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import quickfix.ConfigError;
import quickfix.Initiator;

@RequiredArgsConstructor
@Slf4j
public class FixInitiator {

  private final Initiator initiator;

  @EventListener(ApplicationReadyEvent.class)
  public void onStartup() throws ConfigError {
    initiator.start();
  }

  public void stop() {
    initiator.stop();
  }
}
