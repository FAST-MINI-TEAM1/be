package fast.mini.be;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import fast.mini.be.domain.order.Order;
import fast.mini.be.domain.order.OrderRepository;
import fast.mini.be.domain.order.OrderResponse.orderListByUserDto;
import fast.mini.be.domain.order.OrderRequest;
import fast.mini.be.domain.order.OrderResponse;
import fast.mini.be.domain.order.OrderService;
import fast.mini.be.domain.order.OrderType;
import fast.mini.be.global.jwt.service.JwtService;
import java.util.*;
import javax.transaction.Transactional;



@SpringBootTest
public class OrderServiceTest {

	@Autowired
	private OrderService orderService;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private OrderRepository orderRepository;

	@Transactional
	@Test
	public void testGetUserMainPage() {
		// 이미 존재하는 더미 데이터 조회
		int year = 2023;
		int month = 7;

		// 테스트에 사용할 더미 토큰 생성
		String userEmail = "lphilcock3@latimes.com";
		String token = jwtService.createAccessToken(userEmail);


		// 테스트 메서드 실행
		List<OrderResponse> orderResponses = orderService.getUserMainPage(token, year, month);

		// 테스트 결과 확인
		assertEquals(1, orderResponses.size(),"등록된 객체 수가 예상과 다릅니다.");
		OrderResponse orderResponse = orderResponses.get(0);

	}


	@Test
	public void testAddOrder() {
		// 이미 존재하는 더미 데이터 조회
		OrderType orderType = OrderType.ANNUAL;
		String startAt = "2023-07-14";
		String endAt = "2023-07-18";
		String reason ="이유";
		String category="경조사";
		String etc="기타";


		// 테스트에 사용할 더미 토큰 생성
		String userEmail = "mkellet5@canalblog.com";
		String token = jwtService.createAccessToken(userEmail);

		// OrderRequest 객체 생성
		OrderRequest orderRequest = new OrderRequest();
		orderRequest.setOrderType(orderType);
		orderRequest.setStartAt(startAt);
		orderRequest.setEndAt(endAt);
		orderRequest.setReason(reason);
		orderRequest.setCategory(category);
		orderRequest.setEtc(etc);


		// 테스트 메서드 실행
		orderService.addOrder(token, orderRequest);

		// 주문이 정상적으로 추가되었는지 확인
		List<Order> orders = orderRepository.findByUserEmail(userEmail);
		//기존에 있던 데이터가 5개라 6으로 지정함
		assertEquals(6, orders.size(), "연차가 정상적으로 추가되지 않았습니다.");
		Order order = orders.get(0);

	}
	
	@Test
	public void orderList(){
		// 테스트 데이터 생성
		String userEmail = "mkellet5@canalblog.com";
		String token = jwtService.createAccessToken(userEmail);


		// 테스트 메서드 실행
		Page<orderListByUserDto> orderResponsePage = orderService.getOrdersByUser(token, PageRequest.of(0, 5));

		// 테스트 결과 확인
		assertEquals(orderResponsePage.getNumber(), 0, "현재 페이지는 0번째이다");
		assertEquals(orderResponsePage.getNumberOfElements(), 5, "한 페이지에 데이터는 5개이다.");

		List<orderListByUserDto> content =orderResponsePage.getContent();
		assertEquals(content.size(), 5, "리스트에 5개의 주문 내역이 포함되어 있다.");

	}
}
