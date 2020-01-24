package eu.trixner.base.server.repository;

import eu.trixner.base.server.model.PasswordResetRequest;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.UUID;

public interface PasswordResetRequestRepository extends CrudRepository<PasswordResetRequest, UUID> {
    boolean existsByToken(String token);

    PasswordResetRequest findByToken(String token);

    int deleteByUser_Id(UUID user_Id);

    int deleteByExpiresAtIsBefore(Date date);
}
