package com.neo.gpt_bot_test.exceptions.authError;

import com.neo.gpt_bot_test.exceptions.httpError.UnAuthorizedException;

public class AuthErrorException extends UnAuthorizedException {
  public AuthErrorException(String message) {

    super("Authorization error. " + message);
  }
}
