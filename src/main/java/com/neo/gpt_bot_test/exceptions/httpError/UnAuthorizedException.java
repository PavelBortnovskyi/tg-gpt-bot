package com.neo.gpt_bot_test.exceptions.httpError;

import com.neo.gpt_bot_test.exceptions.AppError;

public class UnAuthorizedException extends AppError {
  public UnAuthorizedException(String msg) {
    super("UNAUTHORIZED. " + msg);
  }
}
