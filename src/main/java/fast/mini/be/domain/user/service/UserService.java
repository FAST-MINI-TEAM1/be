package fast.mini.be.domain.user.service;

import fast.mini.be.domain.user.User;
import fast.mini.be.domain.user.dto.UserLoginRequestDto;
import fast.mini.be.domain.user.dto.UserLoginResponseDto;
import fast.mini.be.domain.user.dto.UserRegisterRequestDto;
import fast.mini.be.domain.user.dto.UserRegisterResponseDto;
import fast.mini.be.global.jwt2.TokenDto;
import javax.servlet.http.HttpServletRequest;

public interface UserService {

    UserRegisterResponseDto register(UserRegisterRequestDto requestDto) throws Exception;

    UserLoginResponseDto login(HttpServletRequest request, UserLoginRequestDto requestDto)
        throws Exception;

    Boolean emailExists(String email);

    TokenDto reIssue(TokenDto requestDto);

    User findUserByToken(TokenDto requestDto);

}
