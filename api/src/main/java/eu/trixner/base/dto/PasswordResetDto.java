package eu.trixner.base.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * PasswordResetDto
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2020-02-06T19:52:40.376+01:00[Europe/Berlin]")

public class PasswordResetDto {
    @JsonProperty("newPassword")
    private String newPassword;

    @JsonProperty("token")
    private String token;

    public PasswordResetDto newPassword(String newPassword) {
        this.newPassword = newPassword;
        return this;
    }

    /**
     * Get newPassword
     *
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

    public PasswordResetDto token(String token) {
        this.token = token;
        return this;
    }

    /**
     * Get token
     *
     * @return token
     */
    @ApiModelProperty(value = "")


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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
                Objects.equals(this.token, passwordResetDto.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(newPassword, token);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class PasswordResetDto {\n");

        sb.append("    newPassword: ").append(toIndentedString(newPassword)).append("\n");
        sb.append("    token: ").append(toIndentedString(token)).append("\n");
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

