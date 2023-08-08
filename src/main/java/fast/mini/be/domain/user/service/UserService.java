package fast.mini.be.domain.user.service;

import fast.mini.be.domain.user.dto.UserLoginRequestDto;
import fast.mini.be.domain.user.dto.UserLoginResponseDto;
import fast.mini.be.domain.user.dto.UserRegisterRequestDto;
import fast.mini.be.domain.user.dto.UserRegisterResponseDto;

public interface UserService {
    UserRegisterResponseDto register(UserRegisterRequestDto requestDto) throws Exception;

    UserLoginResponseDto login2(UserLoginRequestDto requestDto) throws Exception;

    Boolean emailExists(String email);

}
