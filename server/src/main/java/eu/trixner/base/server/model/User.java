package eu.trixner.base.server.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.util.Arrays;
import java.util.Collection;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "APP_USER")
public class User extends BaseEntity implements UserDetails {

    @Column(unique = true)
    @Builder.Default
    private String username = "";

    @Column
    @Builder.Default
    private String password = "";

    @Column
    @Builder.Default
    private String email = "";

    @Column
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private UserType userType = UserType.USER;

    @Column
    @Builder.Default
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
        return isAccountNonLocked();
    }

}
