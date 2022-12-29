package eu.trixner.base.server.exceptions;

/**
 * Exception thrown when a user isn't logged in at the moment, but tries to access resources that require login
 */
public class UserNotAuthenticatedException extends RuntimeException {
}
