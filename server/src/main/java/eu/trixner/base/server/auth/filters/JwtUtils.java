package eu.trixner.base.server.auth.filters;

import eu.trixner.base.server.auth.SecurityConstants;
import eu.trixner.base.server.model.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Slf4j
public class JwtUtils {

    public static boolean isExpired(String token) {
        try {
            return parseToken(token).getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException ex) {
            return true;
        }
    }

    public static Jws<Claims> parseToken(String token) {
        byte[] signingKey = SecurityConstants.JWT_SECRET.getBytes();

        return Jwts.parserBuilder().setSigningKey(signingKey).build()
                .parseClaimsJws(token);
    }

    public static UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = getToken(request);
        try {
            Jws<Claims> parsedToken = parseToken(token);

            String username = parsedToken
                    .getBody()
                    .getSubject();

            List<Role> authorities = ((List<?>) parsedToken.getBody()
                    .get("rol")).stream()
                    .map(r -> Role.valueOf(r.toString()))
                    .toList();

            if (StringUtils.isNotEmpty(username)) {
                return new UsernamePasswordAuthenticationToken(username, null, authorities);
            }
        } catch (ExpiredJwtException exception) {
            log.warn("Request to parse expired JWT : {} failed : {}", token, exception.getMessage());
            TokenHandler.getBlackList().remove(token);
        } catch (UnsupportedJwtException exception) {
            log.warn("Request to parse unsupported JWT : {} failed : {}", token, exception.getMessage());
        } catch (MalformedJwtException exception) {
            log.warn("Request to parse invalid JWT : {} failed : {}", token, exception.getMessage());
        } catch (SignatureException exception) {
            log.warn("Request to parse JWT with invalid signature : {} failed : {}", token, exception.getMessage());
        } catch (IllegalArgumentException exception) {
            log.warn("Request to parse empty or null JWT : {} failed : {}", token, exception.getMessage());
        }

        return null;
    }

    public static String getToken(HttpServletRequest request) {
        String token = request.getHeader(SecurityConstants.TOKEN_HEADER);
        if (StringUtils.isNotEmpty(token) && token.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            return token.replace("Bearer ", "");
        } else {
            return null;
        }
    }
}
