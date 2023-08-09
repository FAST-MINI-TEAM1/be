package fast.mini.be.domain.admin;

import fast.mini.be.domain.approveDate.ApproveDate;
import fast.mini.be.domain.order.Order;
import fast.mini.be.domain.user.User;
import fast.mini.be.global.erros.exception.Exception500;
import fast.mini.be.global.utils.AES256;
import fast.mini.be.global.utils.DateUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;

import java.time.Month;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class AdminResponse {
    private static final AES256 AES256 = new AES256();

    @Getter
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
            this.empName = AES256.decrypt(order.getUser().getEmpName());
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

    @Getter
    public static class MonthCountDTO {
        Long jan;
        Long feb;
        Long mar;
        Long apr;
        Long may;
        Long jun;
        Long jul;
        Long aug;
        Long sept;
        Long oct;
        Long nov;
        Long dec;

        public MonthCountDTO() {
            this.jan = 0L;
            this.feb = 0L;
            this.mar = 0L;
            this.apr = 0L;
            this.may = 0L;
            this.jun = 0L;
            this.jul = 0L;
            this.aug = 0L;
            this.sept = 0L;
            this.oct = 0L;
            this.nov = 0L;
            this.dec = 0L;
        }

        public void count(List<ApproveDate> approveDateList) {
            this.jan = countForMonth(approveDateList, Month.JANUARY);
            this.feb = countForMonth(approveDateList, Month.FEBRUARY);
            this.mar = countForMonth(approveDateList, Month.MARCH);
            this.apr = countForMonth(approveDateList, Month.APRIL);
            this.may = countForMonth(approveDateList, Month.MAY);
            this.jun = countForMonth(approveDateList, Month.JUNE);
            this.jul = countForMonth(approveDateList, Month.JULY);
            this.aug = countForMonth(approveDateList, Month.AUGUST);
            this.sept = countForMonth(approveDateList, Month.SEPTEMBER);
            this.oct = countForMonth(approveDateList, Month.OCTOBER);
            this.nov = countForMonth(approveDateList, Month.NOVEMBER);
            this.dec = countForMonth(approveDateList, Month.DECEMBER);
        }

        private long countForMonth(List<ApproveDate> approveDateList, Month month) {
            return approveDateList.stream()
                    .filter(approveDate -> approveDate.getDate().toLocalDate().getMonth() == month)
                    .count();
        }

        public Long getTotalCount() {
            return jan + feb + mar + apr + may + jun + jul + aug + sept + oct + nov + dec;
        }
    }

    @Getter
    public static class MonthlyUserTotalDTO {
        Long id;
        String empName;
        Long empNo;
        MonthCountDTO month;
        Long total;

        public MonthlyUserTotalDTO(User user, MonthCountDTO monthCountDTO) {
            this.id = user.getId();
            this.empName = AES256.decrypt(user.getEmpName());
            this.empNo = Long.valueOf(user.getEmpNo());
            this.month = monthCountDTO;
            this.total = monthCountDTO.getTotalCount();
        }
    }

    @Getter
    public static class DailyOrderDTO {
        String empName;
        Long empNo;
        String orderType;
        String date;

        private DailyOrderDTO(ApproveDate approveDate) {
            this.empName = AES256.decrypt(approveDate.getUser().getEmpName());
            this.empNo = Long.valueOf(approveDate.getUser().getEmpNo());
            this.orderType = approveDate.getOrder().getOrderType().getLabel();
            this.date = DateUtils.toStringFormat(approveDate.getDate());
        }

        public static List<DailyOrderDTO> fromEntityList(List<ApproveDate> approveDateList) {
            return approveDateList.stream().map(DailyOrderDTO::new).collect(Collectors.toList());
        }
    }

    @Getter
    public static class UserSearchDTO {
        Long id;
        Long empNo;
        String empName;
        String createdAt;

        public UserSearchDTO(User user) {
            this.id = user.getId();
            this.empNo = Long.valueOf(user.getEmpNo());
            try {
                this.empName = AES256.decrypt(user.getEmpName());
            } catch (Exception e) {
                throw new Exception500("서버 오류!");
            }
            this.createdAt = DateUtils.toStringFormat(user.getCreatedAt());
        }
    }

    @Getter
    public static class OrderByUserIdDTO {
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

        private OrderByUserIdDTO(Order order) {
            this.id = order.getId();
            this.empName = AES256.decrypt(order.getUser().getEmpName());
            this.createdAt = DateUtils.toStringFormat(order.getCreatedAt());
            this.orderType = order.getOrderType().getLabel();
            this.status = order.getStatus().getLabel();
            this.startDate = DateUtils.toStringFormat(order.getStartDate());
            this.endDate = DateUtils.toStringFormat(order.getEndDate());
            this.reason = order.getReason();
            this.category = order.getCategory();
            this.etc = order.getEtc();
        }

        public static Page<OrderByUserIdDTO> fromEntityList(Page<Order> orderList) {
            return orderList.map(OrderByUserIdDTO::new);
        }
    }
}