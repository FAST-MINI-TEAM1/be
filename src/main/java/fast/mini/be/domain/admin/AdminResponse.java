package fast.mini.be.domain.admin;

import fast.mini.be.domain.approveDate.ApproveDate;
import fast.mini.be.domain.order.Order;
import fast.mini.be.domain.user.User;
import fast.mini.be.global.utils.DateUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.time.Month;
import java.util.List;

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

    @Getter
    @Setter
    public static class MonthCountDTO{
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
    @Setter
    public static class MonthlyUserTotalDTO {
        Long id;
        String empName;
        String empNo;
        MonthCountDTO month;
        Long total;

        public MonthlyUserTotalDTO(User user,MonthCountDTO monthCountDTO) {
            this.id = user.getId();
            this.empName = user.getEmpName();
            this.empNo = user.getEmpNo();
            this.month =monthCountDTO;
            this.total = monthCountDTO.getTotalCount();
        }
    }
}