package com.accelhack.commons.model.utils;

import com.accelhack.commons.model.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.http.HttpStatus;

import java.util.*;

public class ValidatorUtils {
  public static <Req, Res> ResponseSet<Res> validate(Validator validator, Req var1, Class<?>... var2) {
    // FIXME: change to new switch syntax in java 21
    if (Objects.requireNonNull(var1) instanceof Request<?> request) {
      return request.validate(validator);
    }
    return toResponseSet(validator.validate(var1, var2));
  }

  private static <Req, Res> ResponseSet<Res> toResponseSet(Set<ConstraintViolation<Req>> violations) {
    List<ResponseError> errors = violations.stream().map(ResponseError::build).toList();
    if (!errors.isEmpty()) {
      return ResponseSet.operandError(HttpStatus.OK, errors);
    }
    return null;
  }
}
