package com.accelhack.commons.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.*;

@Getter
@Setter(AccessLevel.PACKAGE)
@NoArgsConstructor
public class ResponseSet<E> {

  private Long timestamp = new Date().getTime();

  // 共通項目
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private HttpStatus status;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Integer statusCode;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String message;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Response<E> result;

  // エラー時の項目
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private List<ResponseError> errors;

  private ResponseSet(Builder<E> builder) {
    // 共通項目
    this.status = builder.getStatus();
    this.statusCode = builder.getStatus().value();
    this.message = builder.getMessage();
    this.result = builder.getResult();
    this.errors = builder.getErrors();
  }

  public boolean hasErrors() {
    return Objects.nonNull(errors) && !errors.isEmpty();
  }

  public String encode() throws JsonProcessingException {
    JsonMapper mapper = new JsonMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    return mapper.writeValueAsString(this);
  }

  public static <E> ResponseSet<E> ok(E entity) {
    return (new Builder<E>()).status(HttpStatus.OK).results(Response.ok(entity)).build();
  }

  public static <E> ResponseSet<E> operandError(HttpStatus status, ResponseError error) {
    return operandError(status, List.of(error));
  }

  public static <E> ResponseSet<E> operandError(HttpStatus status, List<ResponseError> errors) {
    return (new Builder<E>()).status(status).results(Response.error(errors)).build();
  }

  public static <E> ResponseSet<E> error(HttpStatus status, List<ResponseError> errors) {
    return (new Builder<E>()).status(status).errors(errors).build();
  }

  @NoArgsConstructor
  static class Builder<BE> extends ResponseSet<BE> {
    Builder<BE> status(HttpStatus status) {
      this.setStatus(status);
      return this;
    }

    Builder<BE> results(Response<BE> result) {
      this.setResult(result);
      return this;
    }

    Builder<BE> errors(List<ResponseError> errors) {
      this.setErrors(errors);
      return this;
    }

    ResponseSet<BE> build() {
      return new ResponseSet<>(this);
    }
  }
}
