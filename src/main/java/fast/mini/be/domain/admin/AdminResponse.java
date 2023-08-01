package fast.mini.be.domain.admin;

import fast.mini.be.domain.order.Order;
import fast.mini.be.global.utils.DateUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

public class AdminResponse {
    @Getter
    @Setter
    public static class OrderByStatusDTO {
        Long id;
        String empName;
        String createdAt;
        String orderType;
        String status;
        String startDate;
        String endDate;
        String reason;
        String category;
        String etc;

        private OrderByStatusDTO(Order order) {
            this.id = order.getId();
            this.empName = order.getUser().getEmpName();
            this.createdAt = DateUtils.toStringFormat(order.getCreatedAt());
            this.orderType = order.getOrderType().getLabel();
            this.status = order.getStatus().getLabel();
            this.startDate = DateUtils.toStringFormat(order.getStartDate());
            this.endDate = DateUtils.toStringFormat(order.getEndDate());
            this.reason = order.getReason();
            this.category = order.getCategory();
            this.etc = order.getEtc();
        }

        public static Page<OrderByStatusDTO> fromEntityList(Page<Order> orderList) {
            return orderList.map(OrderByStatusDTO::new);
        }
    }
}