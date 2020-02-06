package eu.trixner.base.server.repository;

import eu.trixner.base.server.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    User findByUsernameIgnoreCase(String username);

    User findByUsernameAndEmail(String username, String email);
}
