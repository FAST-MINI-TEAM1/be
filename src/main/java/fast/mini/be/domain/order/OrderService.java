package fast.mini.be.domain.order;


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
}

