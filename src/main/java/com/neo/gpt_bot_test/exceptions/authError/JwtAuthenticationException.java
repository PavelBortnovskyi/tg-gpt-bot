package com.neo.gpt_bot_test.exceptions.authError;

public class JwtAuthenticationException extends AuthErrorException {
  public JwtAuthenticationException(String message) {

    super("JWT token empty or invalid!" + message);
  }
}
