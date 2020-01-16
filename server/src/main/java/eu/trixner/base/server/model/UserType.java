package eu.trixner.base.server.model;

import org.springframework.security.core.GrantedAuthority;

public enum UserType implements GrantedAuthority {
    ADMIN, USER;

    @Override
    public String getAuthority() {
        return this.name();
    }
}
