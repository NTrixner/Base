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
        Authentication auth = JwtUtils.getAuthentication(request);
        if (TokenHandler.getBlackList().contains(token) || auth == null) {
            //Either user is already blacklisted, or the token can not be parsed anyways
            response.setStatus(403);
            return;
        }
        log.info("Logging user {} out", auth.getName());
        TokenHandler.getBlackList().add(token);
        response.setStatus(302);
    }
}
