package eu.trixner.base.server.config;

import eu.trixner.base.server.auth.SecurityConstants;
import eu.trixner.base.server.auth.filters.JwtAuthorizationFilter;
import eu.trixner.base.server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
    private static final String[] AUTH_WHITELIST = {
      // -- swagger ui
      "/h2-console/**",
      "/api/auth/login/**",
      "/api/auth/logout/**",
      "/api/user/registration/**",
      "/api/user/forgotPassword/**",
      "/api/user/forgotPassword/**",
      "/api/user/available/**",
      "/api/user/registration/confirmRegistration/**"
    };

    final UserService userService;
    final JwtAuthorizationFilter jwtAuthorizationFilter;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
          .cors().and()
          .csrf(AbstractHttpConfigurer::disable)
          .authorizeHttpRequests(auth -> auth
            .requestMatchers(AUTH_WHITELIST).permitAll()
            .requestMatchers("/api/**").authenticated()
            .anyRequest().permitAll())
          .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
          .addFilterAfter(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
          .userDetailsService(userService)
          .exceptionHandling(e -> e.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
          .headers(h ->
            //needed for the iframes of h2
            h.frameOptions().disable())
          .build();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration loginConfig = new CorsConfiguration().applyPermitDefaultValues();
        loginConfig.addExposedHeader("Authorization");
        source.registerCorsConfiguration(SecurityConstants.AUTH_LOGIN_URL, loginConfig);
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
      throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
