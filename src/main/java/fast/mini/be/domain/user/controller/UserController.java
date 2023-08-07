package fast.mini.be.domain.user.controller;

import fast.mini.be.domain.user.User;
import fast.mini.be.domain.user.dto.UserRegisterDto;
import fast.mini.be.domain.user.service.UserService;
import fast.mini.be.global.utils.dto.CommonResult;
import fast.mini.be.global.utils.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://54.79.60.180:8080",allowedHeaders = "*")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ResponseService responseService;

    private final int FAIL = -1;

    @PostMapping("/api/register")
    public CommonResult register(@RequestBody UserRegisterDto userRegisterDto) throws Exception {
        try {
            userService.register(userRegisterDto);
            return responseService.getSuccessResult();
        } catch (Exception e) {
            e.printStackTrace();
            return responseService.getFailResult(
                FAIL, e.getMessage()
            );
        }
    }
}
