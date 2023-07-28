package fast.mini.be.domain.order;

import fast.mini.be.domain.BaseTimeEntity;
import fast.mini.be.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Entity
@DynamicInsert
@Table(name = "order_tb")
public class Order extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, length = 6)
    @Enumerated(EnumType.STRING)
    private OrderType orderType;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Column(length = 50)
    private String reason;

    @Column(length = 10)
    private String category;

    @Column(length = 50)
    private String etc;

    @Column(nullable = false, length = 7)
    @ColumnDefault("'WAIT'")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
}
