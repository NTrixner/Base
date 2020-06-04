package eu.trixner.base.server.auth.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter
{
    private static final Logger log = LoggerFactory.getLogger(JwtAuthorizationFilter.class);

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager)
    {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException
    {

        String token = JwtUtils.getToken(request);

        if (token != null && JwtUtils.isExpired(token))
        {
            TokenHandler.getBlackList().remove(token);
        }

        if (TokenHandler.getBlackList().contains(token))
        {
            log.info("Unsuccessful authentication try from {}", request.getRemoteAddr());
            log.info("Token was {}", token);
            response.setStatus(401);
            return;
        }

        Authentication auth = JwtUtils.getAuthentication(request);

        if (auth != null)
        {
            SecurityContextHolder.getContext().setAuthentication(auth);

            log.info("Successful authentication try from username {}, IP Address is {}",
                    auth.getName(),
                    request.getRemoteAddr());
        }
        filterChain.doFilter(request, response);
    }

}
