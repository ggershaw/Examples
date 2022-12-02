package org.gershaw.quickfixj.server;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import quickfix.Acceptor;
import quickfix.ConfigError;

@RequiredArgsConstructor
@Slf4j
public class FixAcceptor {

  private final Acceptor acceptor;

  @EventListener(ApplicationReadyEvent.class)
  public void onStartup() throws ConfigError {
    acceptor.start();
  }

  public void stop() {
    acceptor.stop();
  }

}
