package com.bakkt.starbuckspayment.controller.type;

import com.bakkt.common.validator.EnumValidator;
import com.bakkt.merchant.client.model.PaymentTokenType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(onConstructor = @__({@JsonCreator}))
@JsonInclude(value = Include.NON_EMPTY, content = Include.NON_NULL)
public class ApiStarbucksAuthorizationUrlRequest {

  @NotNull(message = "MISSING_FIELD")
  @Size(min = 1, max = 40, message = "INVALID_SIZE")
  @Pattern(regexp = "^[-a-zA-Z0-9{}]+$", message = "INVALID_FIELD")
  @JsonProperty("merchantCustomerId")
  @Schema(
      name = "merchantCustomerId",
      description = "Merchant Customer Id",
      required = true,
      example = "3cdeee86-60ae-48e2-969b-92bb694c59dc")
  private String merchantCustomerId;

  @NotNull(message = "MISSING_FIELD")
  @Size(min = 1, max = 40, message = "INVALID_SIZE")
  @Pattern(regexp = "^[-a-zA-Z0-9{}]+$", message = "INVALID_FIELD")
  @JsonProperty("merchantTransactionId")
  @Schema(
      name = "merchantTransactionId",
      description = "Merchant Transaction Id",
      required = true,
      example = "535f85ec-ce8e-4ed0-b5b1-fc44abd126e4")
  private String merchantTransactionId;

  @NotNull(message = "MISSING_FIELD")
  @EnumValidator(enumClass = PaymentTokenType.class, ignoreCase = true, message = "INVALID_FIELD")
  @JsonProperty(value = "tokenType")
  @Schema(
      description = "Token Type",
      required = true,
      allowableValues = "LONG_LIVING, ONE_TIME",
      example = "LONG_LIVING")
  private String tokenType;
}
