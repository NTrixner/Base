package eu.trixner.base.server.repository;

import eu.trixner.base.server.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    String USERNAME = "username";
    String IGNORES = "ignores";
    String EMAIL = "email";

    User findByUsernameIgnoreCase(String username);

    User findByUsernameIgnoreCaseAndEmailIgnoreCase(String username, String email);

    @Query(value = "SELECT COUNT(u.id) >= 1 "
      + "from User u "
      + "WHERE upper(u.email) = upper(:" + EMAIL + ") "
      + "AND upper(u.email) NOT IN (:" + IGNORES + ")")
    Boolean emailExists(@Param(EMAIL) String email, @Param(IGNORES) List<String> ignores);

    @Query(value = "SELECT COUNT(u.id) >= 1 "
      + "from User u "
      + "WHERE upper(u.username) = upper(:" + USERNAME + " ) "
      + "AND upper(u.username) NOT IN (:" + IGNORES + " )")
    Boolean usernameExists(@Param(USERNAME) String username, @Param(IGNORES) List<String> ignores);
}
