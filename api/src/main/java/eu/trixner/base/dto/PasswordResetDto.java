package eu.trixner.base.dto;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.UUID;
import org.openapitools.jackson.nullable.JsonNullable;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * PasswordResetDto
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2019-11-22T12:43:10.739+01:00[Europe/Berlin]")

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

