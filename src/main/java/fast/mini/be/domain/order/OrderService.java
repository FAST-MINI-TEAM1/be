package fast.mini.be.domain.order;

import fast.mini.be.domain.order.OrderResponse.orderListByUserDto;
import fast.mini.be.domain.user.User;
import fast.mini.be.domain.user.repository.UserRepository;
import fast.mini.be.global.erros.exception.Exception401;
import fast.mini.be.global.erros.exception.Exception403;
import fast.mini.be.global.erros.exception.Exception404;
import fast.mini.be.global.jwt.service.JwtService;
import fast.mini.be.global.utils.DateUtils;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

		String email = jwtService.extractUsername(token)
			.orElseThrow(() -> new Exception401("유효하지 않는 토큰입니다."));

		YearMonth yearMonth = YearMonth.of(year, month);

		List<Order> orders = orderRepository.findByUserEmail(email);

		List<OrderResponse> orderResponses = orders.stream()
			.filter(order -> {
				LocalDateTime startDate = order.getStartDate();
				YearMonth orderYearMonth = YearMonth.of(startDate.getYear(), startDate.getMonth());
				return yearMonth.equals(orderYearMonth);
			})
			.map(OrderResponse::fromOrder)
			.collect(Collectors.toList());

		return orderResponses;

	}

	public void addOrder(String token, OrderRequest orderRequest) {

		String userEmail = jwtService.extractUsername(token)
			.orElseThrow(() -> new Exception401("유효하지 않은 토큰입니다."));

		User user = userRepository.findByEmail(userEmail)
			.orElseThrow(() -> new Exception401("사용자를 찾을 수 없습니다."));

		int annualCount = user.getAnnualCount();

		if (orderRequest.getOrderType() == OrderType.ANNUAL) {
			if (annualCount <= 0) {
				throw new Exception403("사용 가능한 연차가 부족합니다.");
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

	public Page<orderListByUserDto> getOrdersByUser(String token, Pageable pageable) {

		String email = jwtService.extractUsername(token)
			.orElseThrow(() -> new Exception401("유효하지 않는 토큰입니다."));

		// 주문 목록을 페이징
		Page<Order> userOrderList = orderRepository.findByUserEmail(email, pageable);

		// OrderListByUserDto로 변환하여 Page 객체로 반환
		return orderListByUserDto.fromOrder(userOrderList);
	}

	public void deleteOrderByUser(String token, Long id) {

		String email = jwtService.extractUsername(token)
			.orElseThrow(() -> new Exception401("유효하지 않는 토큰입니다."));

		User user = userRepository.findByEmail(email)
			.orElseThrow(() -> new Exception401("사용자를 찾을 수 없습니다."));

		int annualCount = user.getAnnualCount();

		Order order = orderRepository.findByIdAndUserEmail(id, email)
			.orElseThrow(() -> new Exception404("주문 내역을 찾을 수 없습니다."));

		// 주문 내역이 연차인 경우에만 연차 개수를 증가
		if (order.getOrderType() == OrderType.ANNUAL) {

			annualCount++;

			// 사용자의 연차 개수를 업데이트
			user.setAnnualCount(annualCount);
			userRepository.save(user); // 사용자 정보 업데이트
		}

		orderRepository.delete(order);
	}
}

