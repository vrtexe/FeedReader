package mk.finki.emt.feedreader.users.domain.repository;

import java.util.Optional;
import mk.finki.emt.feedreader.users.domain.models.User;
import mk.finki.emt.feedreader.users.domain.models.UserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The single repository that represents the table of the aggregate root
 * and is used to interact with the database for both entities present in the module 
 */
@Repository
public interface UserRepository extends JpaRepository<User, UserId> {
  /**
   * This query finds the first user by username
   * @param username the username of the user
   * @return the user object that contains all the info
   */
  Optional<User> findFirstByAuthenticationUsername(String username);

  /**
   * This query finds the first user by email
   * @param email the email of the user
   * @return the user object that contains all the info
   */
  Optional<User> findFirstByEmail(String email);
}
