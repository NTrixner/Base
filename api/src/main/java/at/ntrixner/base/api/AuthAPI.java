package at.ntrixner.base.api;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

import javax.ws.rs.Path;

@Api("/auth")
@Path("/auth")
@OpenAPIDefinition(
        info = @Info(
                version = "1.0",
                contact = @Contact(name = "Nikolaus Trixner", url = "nikolaus.trixner.eu", email = "nikolaus@trixner.eu"),
                description = "Auth API test at.ntrixner.base.api for future projects",
                title = "AuthAPI"
        )
)
public interface AuthAPI {
}
