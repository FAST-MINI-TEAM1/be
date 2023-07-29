package fast.mini.be.domain.order;

import fast.mini.be.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
