package eu.trixner.base.server.repository;

import eu.trixner.base.server.model.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, UUID> {

    User findByUsernameIgnoreCase(String username);
}
