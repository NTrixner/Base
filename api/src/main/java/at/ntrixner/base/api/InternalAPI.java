package at.ntrixner.base.api;

import at.ntrixner.base.dto.MessageDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Api("/internal")
@Path("/internal")
@OpenAPIDefinition(
        info = @Info(
                version = "1.0",
                contact = @Contact(name = "Nikolaus Trixner", url = "nikolaus.trixner.eu", email = "nikolaus@trixner.eu"),
                description = "Internal test at.ntrixner.base.api for future projects",
                title = "InternalAPI"
        ),
        security = @SecurityRequirement(name = "cookieLogin")
)
@SecurityScheme(type = SecuritySchemeType.APIKEY, paramName = "cookieLogin", name = "JSESSIONID", in = SecuritySchemeIn.HEADER)
public interface InternalAPI {

    @ApiOperation(
            value = "/greeting",
            httpMethod = "GET",
            response = MessageDTO.class,
            notes = "Returns a simple test message"
    )
    @GET
    @Path("/greeting")
    MessageDTO getInternalMessage();
}
