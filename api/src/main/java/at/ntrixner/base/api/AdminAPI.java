package at.ntrixner.base.api;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

import javax.ws.rs.Path;

@Api("/admin")
@Path("/admin")
@OpenAPIDefinition(
        info = @Info(
                version = "1.0",
                contact = @Contact(name = "Nikolaus Trixner", url = "nikolaus.trixner.eu", email = "nikolaus@trixner.eu"),
                description = "AdminAPI in at.ntrixner.base.api for future projects",
                title = "AdminAPI"
        ),
        security = @SecurityRequirement(name = "cookieLogin")
)
public interface AdminAPI {
}
