package com.SecureBank.backend.advice;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AppExceptionHandler {
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(RuntimeException.class)
  public Map<String, String> handleNotUniqueNameExceptions(RuntimeException ex) {
    return createErrorMap(ex);
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Map<String, String> handleValidationException(MethodArgumentNotValidException ex) {
    Map<String, String> errorMap = new HashMap<>();
    ex.getBindingResult().getFieldErrors()
        .forEach(fieldError -> errorMap.put(fieldError.getField(), fieldError.getDefaultMessage()));
    return errorMap;
  }

  private Map<String, String> createErrorMap(Exception ex) {
    Map<String, String> errorMap = new HashMap<>();
    errorMap.put("An error occured:", ex.getMessage());
    return errorMap;
  }

}