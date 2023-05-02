package com.accelhack.commons.model.utils;

import com.accelhack.commons.model.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
public class ValidatorUtils {
  private final Validator validator;

  public <Req, Res> ResponseSet<Res> validate(Req var1, Class<?>... var2) {
    return switch (var1) {
      case Request<?> request -> request.validate(validator);
      default -> toResponseSet(validator.validate(var1, var2));
    };
  }

  private <Req, Res> ResponseSet<Res> toResponseSet(Set<ConstraintViolation<Req>> violations) {
    List<ResponseError> errors = violations.stream().map(ResponseError::build).toList();
    if (!errors.isEmpty()) {
      return ResponseSet.operandError(HttpStatus.OK, errors);
    }
    return null;
  }
}
