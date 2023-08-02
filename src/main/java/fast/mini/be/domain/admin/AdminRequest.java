package fast.mini.be.domain.admin;

import fast.mini.be.domain.order.OrderStatus;
import fast.mini.be.domain.order.OrderType;
import fast.mini.be.global.erros.exception.Exception400;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

public class AdminRequest {

    @Getter
    @Setter
    public static class OrderUpdateDTO {
        @NotEmpty
        Long id;

        @NotEmpty
        OrderStatus status;

        public OrderUpdateDTO(Long id, String status) {
            this.id = id;
            this.status = OrderStatus.findByLabel(status);
        }
    }

    @Getter
    @Setter
    public static class MonthlyUserTotalDTO {
        @NotEmpty
        OrderType orderType;

        @NotEmpty
        int year;

        public MonthlyUserTotalDTO(String orderType, int year) {
            if (!("duty".equals(orderType)) && !("annual".equals(orderType))) {
                throw new Exception400("url", "잘못된 입력입니다.");
            }

            this.orderType = OrderType.valueOf(orderType.toUpperCase());
            this.year = year;
        }
    }

    @Getter
    @Setter
    public static class DailyOrderListDTO {
        @NotEmpty
        OrderType orderType;

        @NotEmpty
        int year;

        @NotEmpty
        int month;

        public DailyOrderListDTO(String orderType, int year, int month) {
            if (!("duty".equals(orderType)) && !("annual".equals(orderType))) {
                throw new Exception400("url", "잘못된 입력입니다.");
            }

            this.orderType = OrderType.valueOf(orderType.toUpperCase());
            this.year = year;
            this.month = month;
        }
    }
}
