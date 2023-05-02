package com.accelhack.commons.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Getter
@Setter(AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Response<E> {

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Boolean operationStatus;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private E result;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private List<ResponseError> errors;

  private Response(Builder<E> builder) {
    this.operationStatus = builder.getOperationStatus();
    this.result = builder.getResult();
    this.errors = builder.getErrors();
  }

  public static <E> Response<E> ok(E result) {
    return (new Builder<E>()).operationStatus(true).result(result).build();
  }

  public static <E> Response<E> error(ResponseError error) {
    return error(List.of(error));
  }

  public static <E> Response<E> error(List<ResponseError> errors) {
    return (new Builder<E>()).operationStatus(false).errors(errors).build();
  }

  @NoArgsConstructor
  static class Builder<BE> extends Response<BE> {
    Builder<BE> operationStatus(Boolean operationStatus) {
      this.setOperationStatus(operationStatus);
      return this;
    }

    Builder<BE> errors(List<ResponseError> errors) {
      this.setErrors(errors);
      return this;
    }

    Builder<BE> result(BE result) {
      this.setResult(result);
      return this;
    }

    Response<BE> build() {
      return new Response<>(this);
    }
  }
}
