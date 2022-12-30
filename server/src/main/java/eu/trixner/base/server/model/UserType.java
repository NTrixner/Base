package eu.trixner.base.server.model;

public enum UserType {

    USER(
      true,
      Role.ROLE_USER_CAN_SEE_SELF,
      Role.ROLE_USER_CAN_CHANGE_PASSWORD
    ),
    ADMIN(
      false,
      Role.values() //GRANT ALL THE ROLES
    );

    private final Role[] roles;
    private final boolean isDefault;

    UserType(boolean isDefault, Role... roles) {
        this.roles = roles;
        this.isDefault = isDefault;
    }

    public Role[] getRoles() {
        return roles;
    }

    public boolean isDefault() {
        return this.isDefault;
    }
}
