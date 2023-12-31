package fast.mini.be.domain.admin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fast.mini.be.domain.order.OrderStatus;
import fast.mini.be.domain.order.OrderType;
import fast.mini.be.global.erros.exception.Exception400;
import fast.mini.be.global.erros.exception.Exception500;
import fast.mini.be.global.utils.AES256;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


public class AdminRequest {
    private static final AES256 AES256 = new AES256();

    @Getter
    @Setter
    public static class OrderUpdateDTO {
        @NotNull
        Long id;

        String status;

        @JsonIgnore
        OrderStatus orderStatus;

        public OrderUpdateDTO(Long id, String status) {
            this.id = id;
            this.orderStatus = OrderStatus.findByLabel(status);

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

    @Getter
    @Setter
    public static class UserSearchDTO {
        String empName;
        String empNo;

        public UserSearchDTO(String empName, String empNo) {
            try {
                this.empName = (empName == null) ? empName : AES256.encrypt(empName);
            } catch (Exception e) {
                throw new Exception500("서버 오류!");
            }

            this.empNo = empNo;
        }
    }
}
