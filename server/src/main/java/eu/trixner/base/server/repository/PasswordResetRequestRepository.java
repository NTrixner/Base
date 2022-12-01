package eu.trixner.base.server.repository;

import eu.trixner.base.server.model.PasswordResetRequest;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.UUID;

public interface PasswordResetRequestRepository extends CrudRepository<PasswordResetRequest, UUID> {
    int deleteByUserId(UUID userId);

    int deleteByExpiresAtIsBefore(Date date);
}
