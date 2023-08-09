package fast.mini.be.domain.order;

import fast.mini.be.global.jwt2.JwtTokenProvider;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import fast.mini.be.domain.order.OrderResponse.orderListByUserDto;
import fast.mini.be.global.utils.ApiUtils.ApiResult;

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
import fast.mini.be.global.erros.exception.Exception401;
import fast.mini.be.global.utils.ApiUtils;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;
	private final JwtTokenProvider jwtTokenProvider;

	@GetMapping("/main")
	public ResponseEntity<ApiResult<List<OrderResponse>>> getUserMainPage(@Valid @RequestHeader("Authorization") String token,  @RequestParam int year, @RequestParam int month) throws Exception{

		List<OrderResponse> orders = orderService.getUserMainPage(token, year, month);

		return ResponseEntity.ok(ApiUtils.success(orders));
	}

	@PostMapping("/order/add")
	public ResponseEntity<ApiResult<String>> addOrder(@Valid @RequestHeader("Authorization") String token,
		@RequestBody OrderRequest orderRequest) throws Exception{

		orderService.addOrder(token, orderRequest);

		return ResponseEntity.status(HttpStatus.CREATED).body(ApiUtils.success("등록 완료"));
	}

	@GetMapping("/myorder")
	public ResponseEntity<ApiResult<Page<orderListByUserDto>>> getOrdersByUser(@Valid @RequestHeader("Authorization") String token,
		Pageable pageable)throws Exception{

		Page<OrderResponse.orderListByUserDto> orderList = orderService.getOrdersByUser(token, pageable);

		return ResponseEntity.ok(ApiUtils.success(orderList));
	}

	@PostMapping("/order/delete")
	public ResponseEntity<ApiResult<String>> deleteOrderByUser(@Valid @RequestHeader("Authorization") String token, @RequestParam Long id)throws Exception {

		if(id == null) throw new Exception400("id","유효하지 않는 id 입니다.");

		orderService.deleteOrderByUser(token, id);

		return ResponseEntity.ok(ApiUtils.success("삭제 완료")); // 삭제 성공 시 200 OK 응답 반환
	}
}
