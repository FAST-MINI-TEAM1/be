package fast.mini.be.domain.user.repository;

import fast.mini.be.domain.user.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String eamil);

    boolean existsByEmail(String email);

}
