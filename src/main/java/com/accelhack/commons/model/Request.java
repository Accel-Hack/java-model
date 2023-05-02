package com.accelhack.commons.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.*;

@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class Request<Req extends Operand> {
  private Long timestamp = new Date().getTime();

  @NonNull
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Req operand;

  public <Res> ResponseSet<Res> validate(Validator validator, Class<?>... groups) {
    List<ResponseError> requestErrors = toList(validator.validate(this, groups));
    if (!requestErrors.isEmpty()) {
      return ResponseSet.error(HttpStatus.OK, requestErrors);
    }
    List<ResponseError> operandErrors = toList(validator.validate(operand));
    if (!operandErrors.isEmpty()) {
      return ResponseSet.operandError(HttpStatus.OK, requestErrors);
    }
    return null;
  }

  private <T> List<ResponseError> toList(Set<ConstraintViolation<T>> violations) {
    return violations.stream().map(ResponseError::build).toList();
  }
}
