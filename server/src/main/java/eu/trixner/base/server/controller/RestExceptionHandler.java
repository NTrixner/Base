package eu.trixner.base.server.controller;

import eu.trixner.base.server.exceptions.UserNotAuthenticatedException;
import eu.trixner.base.server.exceptions.UserNotAuthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.security.InvalidParameterException;

@ControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({NullPointerException.class})
    protected ResponseEntity<Object> handleNullPointer(RuntimeException ex, WebRequest request) {
        log.error("There was an NPE. The request was: {}", request, ex);
        return ResponseEntity.status(404).body(ex);
    }

    @ExceptionHandler({UsernameNotFoundException.class})
    protected ResponseEntity<Object> handleUserNotFound(RuntimeException ex, WebRequest request) {
        log.error("The requested user was not found. The request was: {}", request, ex);
        return ResponseEntity.status(404).body(ex);
    }

    @ExceptionHandler({UserNotAuthorizedException.class})
    protected ResponseEntity<Object> handleUserNotAuthorized(RuntimeException ex, WebRequest request) {
        log.error("The user tried to perform an action they're not authorized to do. The request was: {}", request, ex);
        return ResponseEntity.status(403).body(ex);
    }

    @ExceptionHandler({UserNotAuthenticatedException.class, BadCredentialsException.class})
    protected ResponseEntity<Object> handleUserNotAuthenticated(RuntimeException ex, WebRequest request) {
        log.error("The user tried to accesss privileged resources, but isn't logged in. The request was: {}", request, ex);
        return ResponseEntity.status(401).body(ex);
    }

    @ExceptionHandler({InvalidParameterException.class})
    protected ResponseEntity<Object> handleInvalidParameter(RuntimeException ex, WebRequest request) {
        log.error("There was a web request with wrong parameters. The request was: {}", request, ex);
        return ResponseEntity.status(400).body(ex);
    }
}
