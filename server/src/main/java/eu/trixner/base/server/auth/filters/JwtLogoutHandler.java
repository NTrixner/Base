package eu.trixner.base.server.auth.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JwtLogoutHandler implements LogoutHandler {
    private static final Logger log = LoggerFactory.getLogger(JwtLogoutHandler.class);

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        log.info("Logout called from {}", request.getRemoteAddr());
        String token = JwtUtils.getToken(request);
        if (!TokenHandler.getTokens().containsKey(token)) {
            log.info("Could not find session");
            response.setStatus(403);
            return;
        }
        log.info("Logging user {} out", TokenHandler.getTokens().get(token).getName());
        TokenHandler.getTokens().remove(token);
    }
}
