package fast.mini.be.domain.admin;

import fast.mini.be.global.erros.exception.Exception400;
import fast.mini.be.global.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final AdminService adminService;

    @PostMapping("/order/update")
    public ResponseEntity<?> orderUpdate(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody AdminRequest.OrderUpdateDTO orderUpdateDTO
    ) {
        adminService.orderUpdate(orderUpdateDTO);
        return new ResponseEntity<>(ApiUtils.success(null), HttpStatus.OK);
    }

    @GetMapping("/order/list/status/{status}")
    public ResponseEntity<?> orderListByStatus(
            @RequestHeader("Authorization") String token,
            @PathVariable("status") String status,
            Pageable pageable
    ) {
        if (!("wait".equals(status)) && !("complete".equals(status))) {
            throw new Exception400("url", "잘못된 입력입니다.");
        }

        Page<AdminResponse.OrderByStatusDTO> orderListByStatusDTO = adminService.orderListByStatus(status, pageable);
        return new ResponseEntity<>(ApiUtils.success(orderListByStatusDTO), HttpStatus.OK);
    }

    @GetMapping("/order/list/monthly/{orderType}")
    public ResponseEntity<?> monthlyUserTotal(
            @RequestHeader("Authorization") String token,
            @PathVariable("orderType") String orderType,
            @RequestParam(value = "year") int year
    ) {
        log.info("월별 리스트 조회: type=" + orderType + "year=" + year);
        AdminRequest.MonthlyUserTotalDTO requestDTO = new AdminRequest.MonthlyUserTotalDTO(orderType, year);
        List<AdminResponse.MonthlyUserTotalDTO> monthlyUserTotal = adminService.monthlyUserTotal(requestDTO);
        return new ResponseEntity<>(ApiUtils.success(monthlyUserTotal), HttpStatus.OK);
    }

    @GetMapping("/order/list/daily/{orderType}")
    public ResponseEntity<?> dailyOrderList(
            @RequestHeader("Authorization") String token,
            @PathVariable("orderType") String orderType,
            @RequestParam(value = "year") int year,
            @RequestParam(value = "month") int month
    ) {
        AdminRequest.DailyOrderListDTO requestDTO = new AdminRequest.DailyOrderListDTO(orderType, year, month);
        List<AdminResponse.DailyOrderDTO> dailyOrderDTOList = adminService.dailyOrderList(requestDTO);
        return new ResponseEntity<>(ApiUtils.success(dailyOrderDTOList), HttpStatus.OK);
    }

    @GetMapping("/user/search")
    public ResponseEntity<?> userSearch(
            @RequestHeader("Authorization") String token,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "empno", required = false) String empNo
    ) {
        log.info("월별 리스트 조회: name=" + name + "empno=" + empNo);
        if ((name != null && empNo != null) || (name == null && empNo == null)) {
            throw new Exception400("url", "잘못된 입력입니다.");
        }

        AdminRequest.UserSearchDTO requestDTO = new AdminRequest.UserSearchDTO(name, empNo);
        AdminResponse.UserSearchDTO userSearchDTO = adminService.userSearch(requestDTO);
        return new ResponseEntity<>(userSearchDTO, HttpStatus.OK);
    }

    @GetMapping("/order/list")
    public ResponseEntity<?> orderListByUserId(
            @RequestHeader("Authorization") String token,
            @RequestParam(value = "user") int userId,
            Pageable pageable
    ) {
        Page<AdminResponse.OrderByUserIdDTO> orderListByUserIdDTO = adminService.orderListByUserId((long) userId, pageable);
        return new ResponseEntity<>(orderListByUserIdDTO, HttpStatus.OK);
    }
}
