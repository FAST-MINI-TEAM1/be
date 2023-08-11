package fast.mini.be.domain.order;



import org.springframework.data.domain.Page;

import fast.mini.be.global.erros.exception.Exception500;
import fast.mini.be.global.utils.AES256;
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
	private String reason;
	private String category;
	private String etc;

	private static final fast.mini.be.global.utils.AES256 AES256 = new AES256();

	public static OrderResponse fromOrder(Order order) {
		return new OrderResponse(
			order.getId(),
			order.getOrderType().getLabel(),
			DateUtils.toStringFormat(order.getStartDate()),
			DateUtils.toStringFormat(order.getEndDate()),
			order.getStatus().getLabel(),
			order.getReason(),
			order.getCategory(),
			order.getEtc()
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
		String category;
		String etc;

		private orderListByUserDto(Order order){
			this.id = order.getId();
			try {
				this.empName = AES256.decrypt(order.getUser().getEmpName());
			} catch (Exception e) {
				throw new Exception500("서버 오류!");
			}
			this.createdAt = DateUtils.toStringFormat(order.getCreatedAt());
			this.orderType = order.getOrderType().getLabel();
			this.status = order.getStatus().getLabel();
			this.startDate = DateUtils.toStringFormat(order.getStartDate());
			this.endDate = DateUtils.toStringFormat(order.getEndDate());
			this.reason = order.getReason();
			this.category = order.getCategory();
			this.etc = order.getEtc();
		}
		public static Page<orderListByUserDto> fromOrder(Page<Order> userOrderList) {

			return userOrderList.map(orderListByUserDto::new);
		}
	}
}
