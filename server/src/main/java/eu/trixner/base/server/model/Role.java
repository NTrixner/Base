package eu.trixner.base.server.model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_USER_CAN_SEE_SELF,
    ROLE_USER_CAN_CHANGE_PASSWORD,
    ROLE_USER_CAN_WATCH_USERLIST,
    ROLE_USER_CAN_CREATE_USER,
    ROLE_USER_CAN_GET_USER_BY_ID;

    @Override
    public String getAuthority() {
        return this.name();
    }

}
