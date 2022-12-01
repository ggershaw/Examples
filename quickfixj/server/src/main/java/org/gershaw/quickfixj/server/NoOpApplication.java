package org.gershaw.quickfixj.server;

import lombok.extern.slf4j.Slf4j;
import quickfix.Application;
import quickfix.Message;
import quickfix.SessionID;

@Slf4j
public class NoOpApplication implements Application {

  @Override
  public void onCreate(SessionID sessionId) {

  }

  @Override
  public void onLogon(SessionID sessionId) {
    log.info("Client Logged on {}", sessionId);
  }

  @Override
  public void onLogout(SessionID sessionId) {

  }

  @Override
  public void toAdmin(Message message, SessionID sessionId) {

  }

  @Override
  public void fromAdmin(Message message, SessionID sessionId) {

  }

  @Override
  public void toApp(Message message, SessionID sessionId) {

  }

  @Override
  public void fromApp(Message message, SessionID sessionId) {
  }
}
