package com.neo.gpt_bot_test.exceptions.authError;

import com.neo.gpt_bot_test.exceptions.httpError.BadRequestException;


public class UserAlreadyRegisteredException extends BadRequestException {
  public UserAlreadyRegisteredException(String message) {

    super("User with " + message + " already registered.");
  }
}
