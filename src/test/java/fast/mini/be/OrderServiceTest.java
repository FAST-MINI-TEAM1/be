package fast.mini.be;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import fast.mini.be.domain.order.OrderResponse;
import fast.mini.be.domain.order.OrderService;
import fast.mini.be.global.jwt.service.JwtService;
import java.util.*;
import javax.transaction.Transactional;


@Transactional
@SpringBootTest
public class OrderServiceTest {


	@Autowired
	private OrderService orderService;

	@Autowired
	private JwtService jwtService;

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
}
