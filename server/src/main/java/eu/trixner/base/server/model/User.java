package eu.trixner.base.server.model;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Collection;
import java.util.Collections;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper=false)
@Entity
public class User extends BaseEntity implements UserDetails {
    @Column(unique=true)
    private String username;
    @Column
    private String password;
    @Column
    private String email;
    @Column
    @Enumerated(EnumType.STRING)
    private UserType userType;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(userType);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
