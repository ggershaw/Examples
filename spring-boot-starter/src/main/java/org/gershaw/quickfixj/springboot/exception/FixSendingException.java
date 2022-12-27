package org.gershaw.quickfixj.springboot.exception;

import quickfix.FieldNotFound;

public class FixSendingException extends Throwable {

  public FixSendingException(String message){
    super(message);
  }

  public FixSendingException(String message, Throwable throwable){
    super(message, throwable);
  }

}
