package at.ntrixner.base.api;

import at.ntrixner.base.dto.MessageDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

import javax.ws.rs.GET;
import javax.ws.rs.Path;


@Api("/base")
@Path("/base")
@OpenAPIDefinition(
        info = @Info(
                version = "1.0",
                contact = @Contact(name = "Nikolaus Trixner", url = "nikolaus.trixner.eu", email = "nikolaus@trixner.eu"),
                description = "Base test at.ntrixner.base.api for future projects",
                title = "BaseAPI"
        )
)
public interface BaseAPI {

    @ApiOperation(
            value = "/test",
            httpMethod = "GET",
            response = MessageDTO.class,
            notes = "Returns a simple test message"
    )
    @GET
    @Path("/test")
    MessageDTO getTestMessage();

    @ApiOperation(
            value = "/appName",
            httpMethod = "GET",
            response = MessageDTO.class,
            notes = "Returns the name of the Application"
    )
    @GET
    @Path("/appName")
    MessageDTO getAppName();
}
