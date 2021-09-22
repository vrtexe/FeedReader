package mk.finki.emt.feedreader.users.domain.repository;

import java.util.Optional;
import mk.finki.emt.feedreader.users.domain.models.User;
import mk.finki.emt.feedreader.users.domain.models.UserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UserId> {
  Optional<User> findFirstByAuthenticationUsername(String username);

  Optional<User> findFirstByEmail(String email);
}
