package fast.mini.be.domain.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findAllByStatus(OrderStatus orderStatus, Pageable pageable);

    Page<Order> findAllByStatusNot(OrderStatus orderStatus, Pageable pageable);
}
