package fast.mini.be.domain.user.repository;

import fast.mini.be.domain.user.Role;
import fast.mini.be.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String eamil);

    boolean existsByEmail(String email);

    Optional<User> findByRefreshToken(String refreshToken);

    Optional<List<User>> findAllByRole(Role role);
}
