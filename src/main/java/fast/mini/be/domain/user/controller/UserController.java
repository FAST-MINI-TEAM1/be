package fast.mini.be.domain.user.controller;

import fast.mini.be.domain.user.dto.UserLoginRequestDto;
import fast.mini.be.domain.user.dto.UserLoginResponseDto;
import fast.mini.be.domain.user.dto.UserRegisterRequestDto;
import fast.mini.be.domain.user.dto.UserRegisterResponseDto;
import fast.mini.be.domain.user.service.UserService;
import fast.mini.be.global.erros.exception.Exception400;
import fast.mini.be.global.erros.exception.Exception403;
import fast.mini.be.global.utils.ApiUtils;
import fast.mini.be.global.utils.dto.CommonResult;
import fast.mini.be.global.utils.service.ResponseService;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegisterRequestDto requestDto)
        throws Exception {
        UserRegisterResponseDto responseDto = userService.register(requestDto);
        return new ResponseEntity<>(ApiUtils.success(responseDto), HttpStatus.CREATED);
    }


    @PostMapping("/login2")
    public ResponseEntity<?> login2(@RequestBody UserLoginRequestDto requestDto)
        throws Exception {
        UserLoginResponseDto responseDto = userService.login2(requestDto);
        return new ResponseEntity<>(ApiUtils.success(responseDto), HttpStatus.OK);
    }

    @PostMapping("/register/emailExists")
    public ResponseEntity<?> emailExists(@RequestBody String email) {
        Boolean res = userService.emailExists(email);
        if (res) {
            throw new Exception403("이미 사용중인 이메일 입니다.");
        } else {
            return new ResponseEntity<>(ApiUtils.success("사용가능한 이메일 입니다."), HttpStatus.OK);
        }
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello";
    }
}
