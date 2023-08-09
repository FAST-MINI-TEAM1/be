package fast.mini.be.domain.loginlog;

import fast.mini.be.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<LoginLog, Long> {

}
