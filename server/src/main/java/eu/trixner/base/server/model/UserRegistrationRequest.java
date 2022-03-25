package eu.trixner.base.server.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
@Entity
public class UserRegistrationRequest extends BaseEntity {
    @OneToOne
    User user;

    @Column
    @Builder.Default
    Date expiresAt = new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000);

    @Column(unique = true)
    @Builder.Default
    String token = "";

    @Column
    @Builder.Default
    Boolean mailSent = false;
}
