package eu.trixner.base.server.auth.filters;

import eu.trixner.base.server.auth.SecurityConstants;
import eu.trixner.base.server.model.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtils {

    private final UserDetailsService userDetailsService;
    private final DateTimeProvider currentDateTimeProvider;
    @Value("${jwt.token.expiration}")
    private int JWT_TOKEN_EXPIRATION;
    @Value("${jwt.token.refresh}")
    private int JWT_TOKEN_REFRESH;
    @Value("${jwt.secret}")
    private String JWT_SECRET;

    public boolean isExpired(String token) {
        try {
            TemporalAccessor temporalAccessor = currentDateTimeProvider.getNow()
              .orElseThrow(() -> new DateTimeException("No date time received"));
            Date date = new Date(Instant.from(temporalAccessor).toEpochMilli());
            return parseToken(token).getBody().getExpiration().before(date);
        } catch (ExpiredJwtException ex) {
            return true;
        }
    }

    public boolean shouldRefresh(String token) {
        try {
            TemporalAccessor temporalAccessor = currentDateTimeProvider.getNow()
              .orElseThrow(() -> new DateTimeException("No date time received"));

            //shouldRefresh = now + refreshPeriod < expiration; same as now < expiration - refreshPeriod
            //AKA if it's refresh millis until expiration would happen, we refresh.
            Date date = new Date(Instant.from(temporalAccessor).toEpochMilli() + JWT_TOKEN_REFRESH);

            Date expiration = parseToken(token).getBody().getExpiration();

            return expiration.before(date);
        } catch (ExpiredJwtException ex) {
            return false;
        }
    }

    public Jws<Claims> parseToken(String token) {
        byte[] signingKey = JWT_SECRET.getBytes();

        return Jwts.parserBuilder().setSigningKey(signingKey).build()
          .parseClaimsJws(token);
    }

    public UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = getToken(request);
        try {

            Jws<Claims> parsedToken = parseToken(token);
            String username = getUsername(parsedToken);
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

    public String getToken(HttpServletRequest request) {
        String token = request.getHeader(SecurityConstants.TOKEN_HEADER);
        if (StringUtils.isNotEmpty(token) && token.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            return token.replace("Bearer ", "");
        } else {
            return null;
        }
    }

    public String getUsername(Jws<Claims> parsedToken) {
        return parsedToken
          .getBody()
          .getSubject();
    }

    public String createToken(String username) {
        UserDetails user = userDetailsService.loadUserByUsername(username);
        byte[] signingKey = JWT_SECRET.getBytes();

        List<String> roles = user.getAuthorities()
          .stream()
          .map(GrantedAuthority::getAuthority)
          .collect(Collectors.toList());

        return Jwts.builder()
          .signWith(Keys.hmacShaKeyFor(signingKey), SignatureAlgorithm.HS512)
          .setHeaderParam("typ", SecurityConstants.TOKEN_TYPE)
          .setIssuer(SecurityConstants.TOKEN_ISSUER)
          .setAudience(SecurityConstants.TOKEN_AUDIENCE)
          .setSubject(user.getUsername())
          .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_EXPIRATION))
          .setId(UUID.randomUUID().toString())
          .claim("rol", roles)
          .compact();
    }

    public String refreshToken(HttpServletRequest request) {
        String token = getToken(request);
        try {

            Jws<Claims> parsedToken = parseToken(token);
            String username = getUsername(parsedToken);
            return createToken(username);
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
}
