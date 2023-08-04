package fast.mini.be.domain.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order,Long> {
	List<Order> findByUserId(Long user_id);

	Page<Order> findByUserId(Long user_id, Pageable pageable);

	Optional<Order> findById(Long id);

	// 주어진 주문 ID에 해당하는 승인 날짜들을 삭제하는 쿼리
	@Modifying
	@Query("DELETE FROM ApproveDate ad WHERE ad.order.id = :orderId")
	void deleteApproveDatesByOrderId(Long orderId);
  
    Page<Order> findAllByStatus(OrderStatus status, Pageable pageable);

    Page<Order> findAllByStatusNot(OrderStatus status, Pageable pageable);

    Page<Order> findAllByUserId(long userId, Pageable pageable);

}
