package fast.mini.be.domain.user.controller;

import fast.mini.be.domain.user.dto.UserLoginRequestDto;
import fast.mini.be.domain.user.dto.UserLoginResponseDto;
import fast.mini.be.domain.user.dto.UserRegisterRequestDto;
import fast.mini.be.domain.user.dto.UserRegisterResponseDto;
import fast.mini.be.domain.user.service.UserService;
import fast.mini.be.global.erros.exception.Exception400;
import fast.mini.be.global.erros.exception.Exception403;
import fast.mini.be.global.jwt2.JwtTokenProvider;
import fast.mini.be.global.jwt2.TokenDto;
import fast.mini.be.global.utils.ApiUtils;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegisterRequestDto requestDto)
        throws Exception {
        UserRegisterResponseDto responseDto = userService.register(requestDto);
        return new ResponseEntity<>(ApiUtils.success(responseDto), HttpStatus.CREATED);
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginRequestDto requestDto, HttpServletRequest request)
        throws Exception {
        UserLoginResponseDto responseDto = userService.login(request, requestDto);
        return new ResponseEntity<>(ApiUtils.success(responseDto), HttpStatus.OK);
    }

    @GetMapping ("/register/emailExists")
    public ResponseEntity<?> emailExists(@RequestBody Map<String, String> emailMap) {
        String email = emailMap.get("email");
        userService.emailExists(email);
        return new ResponseEntity<>(ApiUtils.success("사용이 가능한 이메일입니다."), HttpStatus.OK);
    }

    @GetMapping("/login/reissue")
    public ResponseEntity<?> reIssue(@RequestBody TokenDto requestDto) {
        TokenDto responseDto = userService.reIssue(requestDto);
        return new ResponseEntity<>(ApiUtils.success(responseDto), HttpStatus.OK);
    }

    @GetMapping("/hello")
    public String hello(@RequestHeader("Authorization") String token) {
        String email = jwtTokenProvider.getMemberEmail(token);
        System.out.println(email);
        return email;
    }
}
