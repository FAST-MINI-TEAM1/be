package fast.mini.be.domain.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {
	List<Order> findByUserEmail(String email);

	Optional<Order> findByIdAndUserEmail(Long id, String email);

	Page<Order> findByUserEmail(String email, Pageable pageable);
  
    Page<Order> findAllByStatus(OrderStatus status, Pageable pageable);

    Page<Order> findAllByStatusNot(OrderStatus status, Pageable pageable);
}
