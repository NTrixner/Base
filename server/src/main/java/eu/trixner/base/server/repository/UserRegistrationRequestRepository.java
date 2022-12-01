package eu.trixner.base.server.repository;

import eu.trixner.base.server.model.UserRegistrationRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface UserRegistrationRequestRepository extends CrudRepository<UserRegistrationRequest, UUID> {
    List<UserRegistrationRequest> findByExpiresAtIsBefore(Date date);
}
