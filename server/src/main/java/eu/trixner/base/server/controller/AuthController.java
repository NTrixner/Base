package eu.trixner.base.server.controller;

import eu.trixner.base.dto.LoginDto;
import eu.trixner.base.server.auth.SecurityConstants;
import eu.trixner.base.server.auth.filters.JwtUtils;
import eu.trixner.base.server.auth.filters.TokenHandler;
import eu.trixner.base.server.exceptions.UserNotAuthenticatedException;
import eu.trixner.base.user.AuthApi;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("api")
public class AuthController implements AuthApi {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    @Autowired
    private HttpServletRequest request;

    @Override
    public ResponseEntity<Void> loginUser(@Valid @RequestBody(required = false) LoginDto loginDto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));

        log.info("Login try from IP {} with username {}", request.getRemoteAddr(), loginDto.getUsername());

        UserDetails user = userDetailsService.loadUserByUsername(loginDto.getUsername());
        byte[] signingKey = SecurityConstants.JWT_SECRET.getBytes();

        List<String> roles = user.getAuthorities()
          .stream()
          .map(GrantedAuthority::getAuthority)
          .collect(Collectors.toList());

        String token = Jwts.builder()
          .signWith(Keys.hmacShaKeyFor(signingKey), SignatureAlgorithm.HS512)
          .setHeaderParam("typ", SecurityConstants.TOKEN_TYPE)
          .setIssuer(SecurityConstants.TOKEN_ISSUER)
          .setAudience(SecurityConstants.TOKEN_AUDIENCE)
          .setSubject(user.getUsername())
          .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.JWT_TOKEN_EXPIRATION))
          .setId(UUID.randomUUID().toString())
          .claim("rol", roles)
          .compact();

        return ResponseEntity
          .ok()
          .header(SecurityConstants.TOKEN_HEADER, SecurityConstants.TOKEN_PREFIX + token)
          .build();


    }

    @Override
    public ResponseEntity<Void> logoutUser() {
        log.info("Logout called from {}", request.getRemoteAddr());
        String token = JwtUtils.getToken(request);
        Authentication auth = JwtUtils.getAuthentication(request);
        if (TokenHandler.getBlackList().contains(token) || auth == null) {
            //Either user is already blacklisted, or the token can not be parsed anyways
            throw new UserNotAuthenticatedException();
        }
        log.info("Logging user {} out", auth.getName());
        TokenHandler.getBlackList().add(token);
        return ResponseEntity.status(204).build();
    }
}
