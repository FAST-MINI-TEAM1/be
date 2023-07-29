package fast.mini.be.domain.approveDate;

import fast.mini.be.domain.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApproveDateRepository extends JpaRepository<ApproveDate, Long> {
}
