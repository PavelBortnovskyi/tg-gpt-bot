package com.neo.gpt_bot_test.exceptions;

import lombok.Getter;

@Getter
public class ErrorInfo {
  private final String url;
  private final String info;

  ErrorInfo(String url, String info) {
    this.url = url;
    this.info = info;
  }
}
