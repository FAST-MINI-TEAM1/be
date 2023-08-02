package fast.mini.be.domain.approveDate;

import fast.mini.be.domain.order.OrderType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ApproveDateRepository extends JpaRepository<ApproveDate, Long> {
    @Query("SELECT a FROM ApproveDate a " +
            "WHERE a.user.id = :userId AND a.order.orderType = :orderType AND YEAR(a.date) = :year")
    Optional<List<ApproveDate>> findAllByUserAndOrderTypeInYear(Long userId, OrderType orderType, int year);

    @Query("SELECT a FROM ApproveDate a " +
            "WHERE a.order.orderType = :orderType AND YEAR(a.date) = :year AND MONTH(a.date) = :month")
    Optional<List<ApproveDate>> findAllByOrderTypeInYearMonth(OrderType orderType, int year, int month);
}
