package fast.mini.be.domain.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static fast.mini.be.domain.admin.AdminRequest.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final AdminService adminService;

    @PostMapping("/order/update")
    public ResponseEntity<?> orderUpdate(@Valid @RequestBody orderUpdateDTO orderUpdateDTO){
        adminService.orderUpdate(orderUpdateDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
