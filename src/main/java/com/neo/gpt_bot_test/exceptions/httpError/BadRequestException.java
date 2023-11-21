package com.neo.gpt_bot_test.exceptions.httpError;


import com.neo.gpt_bot_test.exceptions.AppError;

public class BadRequestException extends AppError {
  public BadRequestException(String msg) {
    super("BAD REQUEST. " + msg);
  }
}
