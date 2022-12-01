package eu.trixner.base.server.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
@Entity
public class PasswordResetRequest extends BaseEntity {
    @ManyToOne
    User user;

    @Column
    @Builder.Default
    Date expiresAt = new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000);

    @Column
    @Builder.Default
    Boolean mailSent = false;
}
