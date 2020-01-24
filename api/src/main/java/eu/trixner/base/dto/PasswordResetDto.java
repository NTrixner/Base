package eu.trixner.base.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.UUID;

/**
 * PasswordResetDto
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2020-01-24T16:19:38.798+01:00[Europe/Berlin]")

public class PasswordResetDto   {
  @JsonProperty("newPassword")
  private String newPassword;

  @JsonProperty("requestId")
  private UUID requestId;

  public PasswordResetDto newPassword(String newPassword) {
    this.newPassword = newPassword;
    return this;
  }

  /**
   * Get newPassword
   * @return newPassword
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull


  public String getNewPassword() {
    return newPassword;
  }

  public void setNewPassword(String newPassword) {
    this.newPassword = newPassword;
  }

  public PasswordResetDto requestId(UUID requestId) {
    this.requestId = requestId;
    return this;
  }

  /**
   * Get requestId
   * @return requestId
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid

  public UUID getRequestId() {
    return requestId;
  }

  public void setRequestId(UUID requestId) {
    this.requestId = requestId;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PasswordResetDto passwordResetDto = (PasswordResetDto) o;
    return Objects.equals(this.newPassword, passwordResetDto.newPassword) &&
        Objects.equals(this.requestId, passwordResetDto.requestId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(newPassword, requestId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PasswordResetDto {\n");
    
    sb.append("    newPassword: ").append(toIndentedString(newPassword)).append("\n");
    sb.append("    requestId: ").append(toIndentedString(requestId)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

