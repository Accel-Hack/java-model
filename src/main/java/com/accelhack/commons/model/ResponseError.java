package com.accelhack.commons.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.ConstraintViolation;
import lombok.*;

@Getter
@Setter(AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseError {
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String key;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Object value;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String code;

  public static ResponseError build(String key, Object value, String code) {
    return new ResponseError(key, value, code);
  }

  public static ResponseError build(ConstraintViolation<?> violation) {
    return new ResponseError(violation.getPropertyPath().toString(), violation.getInvalidValue(),
        violation.getMessage());
  }
}
