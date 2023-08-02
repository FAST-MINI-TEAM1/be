package fast.mini.be.domain.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {
	List<Order> findByUserEmail(String email);

	Page<Order> findByUserEmail(String email, Pageable pageable);
  
    Page<Order> findAllByStatus(OrderStatus status, Pageable pageable);

    Page<Order> findAllByStatusNot(OrderStatus status, Pageable pageable);

}
