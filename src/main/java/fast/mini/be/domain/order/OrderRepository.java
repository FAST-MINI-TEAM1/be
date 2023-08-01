package fast.mini.be.domain.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface OrderRepository extends JpaRepository<Order,Long> {
	  List<Order> findByUserEmail(String email);
  
    Page<Order> findAllByStatus(OrderStatus status, Pageable pageable);

    Page<Order> findAllByStatusNot(OrderStatus status, Pageable pageable);
}
