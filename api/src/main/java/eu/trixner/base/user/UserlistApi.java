/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (4.2.1).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package eu.trixner.base.user;

import eu.trixner.base.dto.PaginationRequestDto;
import eu.trixner.base.dto.UserListDto;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.NativeWebRequest;

import javax.validation.Valid;
import java.util.Optional;
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2020-02-06T19:52:40.376+01:00[Europe/Berlin]")

@Validated
@Api(value = "userlist", description = "the userlist API")
public interface UserlistApi {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    @ApiOperation(value = "Your GET endpoint", nickname = "getUserCount", notes = "Returns the amount of users that currently exist.", response = Integer.class, authorizations = {
            @Authorization(value = "auth")
    }, tags = {})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = Integer.class),
            @ApiResponse(code = 403, message = "Unauthorized")})
    @RequestMapping(value = "/userlist/num",
            produces = {"application/json"},
            method = RequestMethod.GET)
    default ResponseEntity<Integer> getUserCount() {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    @ApiOperation(value = "Your GET endpoint", nickname = "listUsers", notes = "Return a paginated list of all users. If the provided pagination is not correct, the first 20 users will be returned instead.", response = UserListDto.class, authorizations = {
            @Authorization(value = "auth")
    }, tags = {})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = UserListDto.class),
            @ApiResponse(code = 403, message = "Unauthorized")})
    @RequestMapping(value = "/userlist",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.GET)
    default ResponseEntity<UserListDto> listUsers(@ApiParam(value = "The Pagination Request. Is ignored if null."  )  @Valid @RequestBody PaginationRequestDto paginationRequestDto) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"ordering\" : \"ordering\", \"pageSize\" : 0, \"items\" : [ { \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"email\" : \"email\", \"username\" : \"username\" }, { \"id\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\", \"email\" : \"email\", \"username\" : \"username\" } ], \"pagePos\" : 6 }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

}
