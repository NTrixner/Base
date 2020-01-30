package eu.trixner.base.server.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Arrays;
import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
@Entity
public class User extends BaseEntity implements UserDetails {
    @Column(unique = true)
    private String username = "";
    @Column
    private String password = "";
    @Column
    private String email = "";
    @Column
    @Enumerated(EnumType.STRING)
    private UserType userType = UserType.USER;
    @Column
    private Boolean isActivated = false;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(userType.getRoles());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isActivated;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActivated;
    }
}
