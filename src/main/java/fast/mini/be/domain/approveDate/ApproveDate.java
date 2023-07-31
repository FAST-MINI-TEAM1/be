package fast.mini.be.domain.approveDate;

import fast.mini.be.domain.order.Order;
import fast.mini.be.domain.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "approve_date_tb")
public class ApproveDate {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private LocalDateTime date;
}
