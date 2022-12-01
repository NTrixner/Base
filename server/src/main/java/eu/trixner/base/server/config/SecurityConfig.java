package eu.trixner.base.server.config;

import eu.trixner.base.server.auth.SecurityConstants;
import eu.trixner.base.server.auth.filters.JwtAuthenticationFilter;
import eu.trixner.base.server.auth.filters.JwtAuthorizationFilter;
import eu.trixner.base.server.auth.filters.JwtLogoutHandler;
import eu.trixner.base.server.auth.filters.JwtLogoutSuccessHandler;
import eu.trixner.base.server.controller.MainController;
import eu.trixner.base.server.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityConfig.class);

    private static final String[] AUTH_WHITELIST = {
      // -- swagger ui
      "/h2-console/**",
      "/user/registration/**",
      "/user/forgotPassword/**",

      // Static content when the client is served from the server application
      MainController.MAIN_PAGE_URL,
      "/login",
      "/*.js",
      "/*.js.map", //TODO: Right now we still create sourcemaps even in prod build. In tsconfig we should exclude it if we are in production mode
      "/user/forgotPassword/**",
      "/user/available/**",
      "/user/registration/confirmRegistration/**"
    };

    final UserService userService;

    @Autowired
    public SecurityConfig(UserService userService) {
        this.userService = userService;
    }


    @Bean
    public AuthenticationManager authenticationManager(
      final AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(userService.passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager manager) throws Exception {
        return http
          .cors().and()
          .csrf().disable()
          .authorizeHttpRequests()
          .requestMatchers(AUTH_WHITELIST).permitAll()
          .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
          .requestMatchers(PathRequest.toH2Console()).permitAll()
          .anyRequest().authenticated()
          .and()
          .addFilter(new JwtAuthenticationFilter(manager))
          .addFilter(new JwtAuthorizationFilter(manager))
          .sessionManagement()
          .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
          .and()
          .authenticationManager(manager)
          .logout()
          .logoutUrl(SecurityConstants.AUTH_LOGOUT_URL)
          .addLogoutHandler(new JwtLogoutHandler())
          .logoutSuccessHandler(new JwtLogoutSuccessHandler())
          .invalidateHttpSession(true)
          .clearAuthentication(true)
          .and()
          .exceptionHandling(e -> e.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
          .headers()
          .frameOptions()
          .disable()
          .and()
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
}
