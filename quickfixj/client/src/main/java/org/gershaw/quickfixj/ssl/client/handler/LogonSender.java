package org.gershaw.quickfixj.ssl.client.handler;

import lombok.SneakyThrows;
import quickfix.ApplicationFunctionalAdapter;
import quickfix.Message;
import quickfix.field.MsgType;
import quickfix.field.Password;
import quickfix.field.Username;

public class LogonSender {

  public LogonSender(final ApplicationFunctionalAdapter functionalAdapter,
      final String user,
      final String pswd) {
    functionalAdapter.addToAdminListener(
        (message, sessionId) -> addLogonFields(message, user, pswd));
  }

  @SneakyThrows
  private void addLogonFields(final Message message,
      final String user,
      final String pswd) {

    final MsgType msgType = new MsgType();
    message.getHeader().getField(msgType);

    if (MsgType.LOGON.equals(msgType.getValue())) {
      message.setField(new Username(user));
      message.setField(new Password(pswd));
    }
  }

}
