package fast.mini.be.domain.order;


import fast.mini.be.domain.user.User;
import fast.mini.be.domain.user.repository.UserRepository;
import fast.mini.be.global.jwt.service.JwtService;
import fast.mini.be.global.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

	private final OrderRepository orderRepository;
	private final UserRepository userRepository;
	private final JwtService jwtService;

	public List<OrderResponse> getUserMainPage(String token, int year, int month) {

		String email = jwtService.extractUsername(token).orElseThrow(() -> new RuntimeException("유효하지 않는 토큰입니다."));

		YearMonth yearMonth = YearMonth.of(year, month);

		List<Order> orders = orderRepository.findByUserEmail(email);

		List<OrderResponse> orderResponses = orders.stream()
			.filter(order -> {
				LocalDateTime startDate = order.getStartDate();
				YearMonth orderYearMonth = YearMonth.of(startDate.getYear(), startDate.getMonth());
				return yearMonth.equals(orderYearMonth);
			})
			.map(order -> new OrderResponse(
				order.getId(),
				order.getOrderType().getLabel(),
				DateUtils.toStringFormat(order.getStartDate()),
				DateUtils.toStringFormat(order.getEndDate()),
				order.getStatus().getLabel()))
			.collect(Collectors.toList());

		return orderResponses;

	}

	public void addOrder(String token, OrderRequest orderRequest) {

		String userEmail = jwtService.extractUsername(token).orElseThrow(() -> new RuntimeException("유효하지 않은 토큰입니다."));

		User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

		int annualCount = user.getAnnualCount();

		if (orderRequest.getOrderType() == OrderType.ANNUAL) {
			if (annualCount <= 0) {
				throw new RuntimeException("사용 가능한 연차가 부족합니다.");
			}
			annualCount--;
		}

		// 사용자의 연차 개수를 업데이트
		user.setAnnualCount(annualCount);
		userRepository.save(user);

		// "yyyy-MM-dd" 형식의 문자열을 LocalDateTime으로 변환
		LocalDateTime startDate = LocalDateTime.parse(orderRequest.getStartAt() + "T00:00:00");
		LocalDateTime endDate = LocalDateTime.parse(orderRequest.getEndAt() + "T00:00:00");


		Order order = Order.builder()
			.user(user)
			.orderType(orderRequest.getOrderType())
			.startDate(startDate)
			.endDate(endDate)
			.reason(orderRequest.getReason())
			.category(orderRequest.getCategory())
			.etc(orderRequest.getEtc())
			.status(OrderStatus.WAIT)
			.build();

		orderRepository.save(order);
	}
}

