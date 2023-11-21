package com.neo.gpt_bot_test.exceptions.authError;

public class EmailNotFoundException extends AuthErrorException {
  public EmailNotFoundException(String message) {
    super("Email not found." + message);
  }
}
