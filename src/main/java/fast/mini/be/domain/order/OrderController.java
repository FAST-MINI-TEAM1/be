package fast.mini.be.domain.order;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import fast.mini.be.domain.order.OrderResponse.orderListByUserDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fast.mini.be.global.erros.exception.Exception400;
import fast.mini.be.global.jwt.service.JwtService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;
	private final JwtService jwtService;

	@GetMapping("/main")
	public ResponseEntity<List<OrderResponse>> getUserMainPage(@Valid @RequestHeader("Authorization") String token,  @RequestParam int year, @RequestParam int month) {

		String jwtToken = token.replace("Bearer ", "");

		Optional<String> userEmailOpt = jwtService.extractUsername(jwtToken);

		if (userEmailOpt.isEmpty()) {
			//토큰이 유효하지 않는 경우 401 반환
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		String userEmail = userEmailOpt.get();

		List<OrderResponse> orders = orderService.getUserMainPage(userEmail, year, month);

		return ResponseEntity.ok(orders);
	}

	@PostMapping("/order/add")
	public ResponseEntity<String> addOrder(@Valid @RequestHeader("Authorization") String token,
		@RequestBody OrderRequest orderRequest) {

		orderService.addOrder(token, orderRequest);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping("/myorder")
	public ResponseEntity<Page<orderListByUserDto>> getOrdersByUser(@Valid @RequestHeader("Authorization") String token,
		Pageable pageable){

		Page<OrderResponse.orderListByUserDto> orderList = orderService.getOrdersByUser(token, pageable);

		return ResponseEntity.ok(orderList);
	}

	@PostMapping("/order/delete/{id}")
	public ResponseEntity<?> deleteOrderByUser(@Valid @RequestHeader("Authorization") String token, @RequestParam Long id) {

		if(id == null) throw new Exception400("id","유효하지 않는 id 입니다.");

		orderService.deleteOrderByUser(token, id);

		return ResponseEntity.ok().build(); // 삭제 성공 시 200 OK 응답 반환
	}
}
