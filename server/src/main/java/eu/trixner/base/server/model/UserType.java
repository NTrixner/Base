package eu.trixner.base.server.model;

import java.util.Arrays;
import java.util.List;

public enum UserType {
    ADMIN(
            Role.values() //GRANT ALL THE ROLES
    ),
    USER(
            Role.ROLE_USER_CAN_SEE_SELF,
            Role.ROLE_USER_CAN_CHANGE_PASSWORD
    );

    private final Role[] roles;

    UserType(Role... roles) {
        this.roles = roles;
    }

    public List<String> getRolesStrings() {
        return Arrays.stream(roles).map(Role::getAuthority).toList();
    }

    public Role[] getRoles() {
        return roles;
    }
}
