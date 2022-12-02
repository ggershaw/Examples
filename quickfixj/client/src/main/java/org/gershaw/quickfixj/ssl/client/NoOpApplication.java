package org.gershaw.quickfixj.ssl.client;

import lombok.extern.slf4j.Slf4j;
import quickfix.Message;
import quickfix.SessionID;

@Slf4j
public class NoOpApplication implements quickfix.Application {

  @Override
  public void onCreate(SessionID sessionId) {

  }

  @Override
  public void onLogon(SessionID sessionId) {
    log.info("Logged on as {}", sessionId);
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
