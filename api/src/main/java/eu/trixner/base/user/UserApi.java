/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (4.2.1).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package eu.trixner.base.user;

import eu.trixner.base.dto.ChangePasswordDto;
import eu.trixner.base.dto.ForgotPasswordDto;
import eu.trixner.base.dto.PasswordResetDto;
import eu.trixner.base.dto.RegistrationDto;
import eu.trixner.base.dto.UserDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.NativeWebRequest;

import javax.validation.Valid;
import java.util.Optional;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2020-01-30T19:25:34.506+01:00[Europe/Berlin]")

@Validated
@Api(value = "user", description = "the user API")
public interface UserApi {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    @ApiOperation(value = "Your GET endpoint", nickname = "changePassword", notes = "Call to change the user's password", authorizations = {
            @Authorization(value = "auth")
    }, tags = {})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Password was changed"),
            @ApiResponse(code = 400, message = "Bad Request, old password was most likely wrong")})
    @RequestMapping(value = "/user/changePassword",
            consumes = {"application/json"},
            method = RequestMethod.POST)
    default ResponseEntity<Void> changePassword(@ApiParam(value = "") @Valid @RequestBody ChangePasswordDto changePasswordDto) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    @ApiOperation(value = "", nickname = "confirmRegistration", notes = "Confirms the registration of a new user by activating via a link that was sent via email.", tags = {})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request")})
    @RequestMapping(value = "/user/registration/confirmRegistration/{token}",
            method = RequestMethod.GET)
    default ResponseEntity<Void> confirmRegistration(@ApiParam(value = "The registration token that was sent via mail to the new user's address", required = true) @PathVariable("token") String token) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    @ApiOperation(value = "Your GET endpoint", nickname = "forgotPassword", notes = "Call if the user forgot their password and want to get sent a mail with a password change link", tags = {})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Mail was sent"),
            @ApiResponse(code = 404, message = "User/Mail combination not found")})
    @RequestMapping(value = "/user/forgotPassword",
            consumes = {"application/json"},
            method = RequestMethod.GET)
    default ResponseEntity<Void> forgotPassword(@ApiParam(value = "") @Valid @RequestBody ForgotPasswordDto forgotPasswordDto) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    @ApiOperation(value = "Your GET endpoint", nickname = "getCurrentUser", notes = "Returns the current user", response = UserDto.class, authorizations = {
            @Authorization(value = "auth")
    }, tags = {})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = UserDto.class),
            @ApiResponse(code = 403, message = "Unauthorized")})
    @RequestMapping(value = "/user",
            produces = {"application/json"},
            method = RequestMethod.GET)
    default ResponseEntity<UserDto> getCurrentUser() {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"email\" : \"email\", \"username\" : \"username\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    @ApiOperation(value = "Get a specific user", nickname = "getUserById", notes = "Returns a specific user", response = UserDto.class, authorizations = {
            @Authorization(value = "auth")
    }, tags = {})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = UserDto.class),
            @ApiResponse(code = 403, message = "Not authorized"),
            @ApiResponse(code = 404, message = "User not found")})
    @RequestMapping(value = "/user/{userId}",
            produces = {"application/json"},
            method = RequestMethod.GET)
    default ResponseEntity<UserDto> getUserById(@ApiParam(value = "The user ID of the user, or null for the currently logged in user",required=true) @PathVariable("userId") String userId) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"email\" : \"email\", \"username\" : \"username\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    @ApiOperation(value = "", nickname = "registerUser", notes = "Registers a new user by putting in username, email and password", tags = {})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created")})
    @RequestMapping(value = "/user/registration/register",
            consumes = {"application/json"},
            method = RequestMethod.POST)
    default ResponseEntity<Void> registerUser(@ApiParam(value = "") @Valid @RequestBody RegistrationDto registrationDto) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    @ApiOperation(value = "Your GET endpoint", nickname = "resetPasswordRequest", notes = "Resets a password based on a password reset request", tags = {})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 404, message = "Not Found")})
    @RequestMapping(value = "/user/forgotPassword/resetPassword",
        consumes = { "application/json" },
        method = RequestMethod.GET)
    default ResponseEntity<Void> resetPasswordRequest(@ApiParam(value = ""  )  @Valid @RequestBody PasswordResetDto passwordResetDto) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

}
