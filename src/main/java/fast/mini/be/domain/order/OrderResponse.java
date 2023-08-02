package fast.mini.be.domain.order;



import org.springframework.data.domain.Page;

import fast.mini.be.global.utils.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OrderResponse {
	private Long id;
	private String orderType;
	private String startDate;
	private String endDate;
	private String status;

	public static OrderResponse fromOrder(Order order) {
		return new OrderResponse(
			order.getId(),
			order.getOrderType().getLabel(),
			DateUtils.toStringFormat(order.getStartDate()),
			DateUtils.toStringFormat(order.getEndDate()),
			order.getStatus().getLabel()
		);
	}

	@Getter
	@Setter
	public static class orderListByUserDto {
		Long id;
		String empName;
		String createdAt;
		String orderType;
		String status;
		String startDate;
		String endDate;
		String reason;

		private orderListByUserDto(Order order){
			this.id = order.getId();
			this.empName = order.getUser().getEmpName();
			this.createdAt = DateUtils.toStringFormat(order.getCreatedAt());
			this.orderType = order.getOrderType().getLabel();
			this.status = order.getStatus().getLabel();
			this.startDate = DateUtils.toStringFormat(order.getStartDate());
			this.endDate = DateUtils.toStringFormat(order.getEndDate());
			this.reason = order.getReason();
		}
		public static Page<orderListByUserDto> fromOrder(Page<Order> userOrderList) {

			return userOrderList.map(orderListByUserDto::new);
		}
	}
}
