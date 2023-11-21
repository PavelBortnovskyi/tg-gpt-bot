package com.neo.gpt_bot_test.exceptions;

import com.neo.gpt_bot_test.exceptions.httpError.BadRequestException;
import com.neo.gpt_bot_test.exceptions.httpError.UnAuthorizedException;
import com.neo.gpt_bot_test.validation.ValidationErrorResponse;
import com.neo.gpt_bot_test.validation.Violation;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Main exception handler for http requests
 */
@Log4j2
@RestControllerAdvice
public class GeneralExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(UnAuthorizedException.class)
  public ErrorInfo handleLoginException(RuntimeException ex, HttpServletRequest request, HttpServletResponse response) {
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    return new ErrorInfo(UrlUtils.buildFullRequestUrl(request), ex.getMessage());
  }

  @ExceptionHandler(BadRequestException.class)
  public ErrorInfo handleBadRequestException(RuntimeException ex, HttpServletRequest request, HttpServletResponse response) {
    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    return new ErrorInfo(UrlUtils.buildFullRequestUrl(request), ex.getMessage());
  }

  // -------- SPRING ---------

  @ExceptionHandler(AuthenticationException.class)
  public ErrorInfo handleAuthException(RuntimeException ex, HttpServletRequest request, HttpServletResponse response) {
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    log.error("Wrong login or password!");
    return new ErrorInfo(UrlUtils.buildFullRequestUrl(request), ex.getMessage());
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(ConstraintViolationException.class)
  public ValidationErrorResponse onConstraintValidationException(ConstraintViolationException e) {
    final List<Violation> violations = e.getConstraintViolations().stream()
      .map(violation -> new Violation(violation.getPropertyPath().toString(), violation.getMessage()))
      .collect(Collectors.toList());
    return new ValidationErrorResponse(violations);
  }

//  @ResponseStatus(HttpStatus.BAD_REQUEST)
//  @ExceptionHandler(MethodArgumentNotValidException.class)
//  public ValidationErrorResponse onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
//    final List<Violation> violations = e.getBindingResult().getFieldErrors().stream()
//      .map(error -> new Violation(error.getField(), error.getDefaultMessage()))
//      .collect(Collectors.toList());
//    return new ValidationErrorResponse(violations);
//  }
}

