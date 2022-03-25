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
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
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
            "/user/available/**"
    };

    final UserService userService;

    @Autowired
    public SecurityConfig(UserService userService)
    {
        this.userService = userService;
    }


    @Override
    public void configure(HttpSecurity http) throws Exception
    {
        http
                .cors().and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(AUTH_WHITELIST).permitAll()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(new JwtAuthenticationFilter(authenticationManager()))
                .addFilter(new JwtAuthorizationFilter(authenticationManager()))
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .logout()
                .logoutUrl(SecurityConstants.AUTH_LOGOUT_URL)
                .addLogoutHandler(new JwtLogoutHandler())
                .logoutSuccessHandler(new JwtLogoutSuccessHandler())
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .and()
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .headers().frameOptions().disable();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception
    {
        auth.userDetailsService(userService).passwordEncoder(userService.passwordEncoder());
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource()
    {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration loginConfig = new CorsConfiguration().applyPermitDefaultValues();
        loginConfig.addExposedHeader("Authorization");
        source.registerCorsConfiguration(SecurityConstants.AUTH_LOGIN_URL, loginConfig);
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }
}
