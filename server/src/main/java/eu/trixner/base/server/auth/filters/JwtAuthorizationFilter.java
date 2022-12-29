package eu.trixner.base.server.auth.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {

        String token = jwtUtils.getToken(request);

        if (token != null && jwtUtils.isExpired(token)) {
            TokenHandler.getBlackList().remove(token);
        }

        if (TokenHandler.getBlackList().contains(token)) {
            log.info("Unsuccessful authentication try from {}", request.getRemoteAddr());
            log.info("Token was {}", token);
            response.setStatus(401);
            return;
        }

        if (token != null) {
            UsernamePasswordAuthenticationToken auth = jwtUtils.getAuthentication(request);
            if (auth != null) {

                SecurityContextHolder.getContext().setAuthentication(auth);

                if (jwtUtils.shouldRefresh(token)) {
                    log.info("Refreshing token");
                    response.setHeader("Authorization", jwtUtils.refreshToken(request));
                    TokenHandler.getBlackList().add(token);
                }
                log.info("Successful authentication try from username {}, IP Address is {}",
                  auth.getName(),
                  request.getRemoteAddr());
            }
        }
        filterChain.doFilter(request, response);
    }

}
