package eu.trixner.base.server.auth;

import org.springframework.beans.factory.annotation.Value;

public class SecurityConstants {

    @Value("${jwt.token.expiration}")
    public static final int JWT_TOKEN_EXPIRATION = 90000;

    public static final String AUTH_LOGIN_URL = "/auth/login";
    public static final String AUTH_LOGOUT_URL = "/auth/logout";

    // Signing key for HS512 algorithm
    // You can use the page http://www.allkeysgenerator.com/ to generate all kinds of keys
    public static final String JWT_SECRET = "n2r5u8x/A%D*G-KaPdSgVkYp3s6v9y$B&E(H+MbQeThWmZq4t7w!z%C*F-J@NcRf";

    // JWT token defaults
    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String TOKEN_TYPE = "JWT";
    public static final String TOKEN_ISSUER = "secure-api";
    public static final String TOKEN_AUDIENCE = "secure-app";

    private SecurityConstants() {
        throw new IllegalStateException("Cannot create instance of static util class");
    }

    public static final String ROLE_PREFIX = "ROLE_";
}
