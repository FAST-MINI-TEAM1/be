package fast.mini.be.domain.admin;

import fast.mini.be.global.erros.exception.Exception400;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final AdminService adminService;

    @PostMapping("/order/update")
    public ResponseEntity<?> orderUpdate(@Valid @RequestBody AdminRequest.OrderUpdateDTO orderUpdateDTO){
        adminService.orderUpdate(orderUpdateDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/order/list/{status}")
    public ResponseEntity<?> orderListByStatus(@PathVariable("status") String status, Pageable pageable) {
        if(!("wait".equals(status)) && !("complete".equals(status))){
            throw new Exception400("url","잘못된 입력입니다.");
        }

        Page<AdminResponse.OrderByStatusDTO> orderListByStatusDTO = adminService.orderListByStatus(status, pageable);
        return new ResponseEntity<>(orderListByStatusDTO,HttpStatus.OK);
    }

    @GetMapping("/order/list/monthly/{orderType}")
    public ResponseEntity<?> monthlyUserTotal(@PathVariable("orderType") String orderType, @RequestParam(value = "year")int year) {
        if(!("duty".equals(orderType)) && !("annual".equals(orderType))){
            throw new Exception400("url","잘못된 입력입니다.");
        }

        List<AdminResponse.MonthlyUserTotalDTO> monthlyUserTotal = adminService.monthlyUserTotal(new AdminRequest.MonthlyUserTotalDTO(orderType, year));
        return new ResponseEntity<>(monthlyUserTotal,HttpStatus.OK);
    }
}
