package eu.trixner.base.server.auth.filters;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Slf4j
public class JwtLogoutHandler implements LogoutHandler
{
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
    {
        log.info("Logout called from {}", request.getRemoteAddr());
        String token = JwtUtils.getToken(request);
        Authentication auth = JwtUtils.getAuthentication(request);
        if (TokenHandler.getBlackList().contains(token) || auth == null)
        {
            //Either user is already blacklisted, or the token can not be parsed anyways
            response.setStatus(401);
            return;
        }
        log.info("Logging user {} out", auth.getName());
        TokenHandler.getBlackList().add(token);
        response.setStatus(204);
    }
}
