package org.gershaw.quickfixj.ssl.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import quickfix.ConfigError;
import quickfix.Initiator;
import quickfix.Message;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionNotFound;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class FixInitiator {

  private final Initiator initiator;

  @EventListener(ApplicationReadyEvent.class)
  public void onStartup() throws ConfigError {
    initiator.start();
  }

  /**
   * For simplicity, I'm assuming you are only sending each FIX message to one session. I only know
   * of 1 venue that requires you to send each msg to multiple sessions. Shh :)
   * @param msg FIX
   * @return boolean to tell if the msg was successfully sent.
   */
  public boolean send(final Message msg){
    final List<SessionID> sessions = initiator.getSessions();

    for (SessionID sessionId : sessions) {
      try {
        return Session.sendToTarget(msg, sessionId);
      } catch (SessionNotFound e) {
        log.error("Unable to send to SessionId: {}. Session is not found. "
            + "Will attempt to send to next Session", sessionId);
      }
    }
    return false;
  }

  public void stop() {
    initiator.stop();
  }
}
