package com.neo.gpt_bot_test.exceptions.authError;

import com.neo.gpt_bot_test.exceptions.httpError.BadRequestException;

public class UsernameIsTakenException extends BadRequestException {
  public UsernameIsTakenException(String message) {

    super("This username is already registered. Please choose another one." + message);
  }
}
