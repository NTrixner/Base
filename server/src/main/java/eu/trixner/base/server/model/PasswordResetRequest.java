package eu.trixner.base.server.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
@Entity
public class PasswordResetRequest extends BaseEntity {
    @ManyToOne
    User user;

    @Column
    Date expiresAt = new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000);

    @Column(unique = true)
    String token = "";

    @Column
    Boolean mailSent = false;
}
