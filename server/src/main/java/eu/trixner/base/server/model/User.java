package eu.trixner.base.server.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@Entity
public class User extends BaseEntity {
    @Column(unique=true)
    private String username;
    @Column
    private String password;
    @Column
    private String email;
}
