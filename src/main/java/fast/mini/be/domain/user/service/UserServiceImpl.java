package fast.mini.be.domain.user.service;

import fast.mini.be.domain.loginlog.LogRepository;
import fast.mini.be.domain.loginlog.LoginLog;
import fast.mini.be.domain.user.Role;
import fast.mini.be.domain.user.User;
import fast.mini.be.domain.user.dto.UserLoginRequestDto;
import fast.mini.be.domain.user.dto.UserLoginResponseDto;
import fast.mini.be.domain.user.dto.UserRegisterRequestDto;
import fast.mini.be.domain.user.dto.UserRegisterResponseDto;
import fast.mini.be.domain.user.repository.UserRepository;
import fast.mini.be.global.erros.exception.Exception400;
import fast.mini.be.global.erros.exception.Exception401;
import fast.mini.be.global.erros.exception.Exception403;
import fast.mini.be.global.jwt2.JwtTokenProvider;
import fast.mini.be.global.jwt2.TokenDto;
import fast.mini.be.global.utils.AES256;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.id.uuid.Helper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final LogRepository logRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AES256 aes256;


    @Transactional
    @Override
    public UserRegisterResponseDto register(UserRegisterRequestDto requestDto) throws Exception {

        String email = requestDto.getEmail();
        String password = requestDto.getPassword();
        String empName = requestDto.getEmpName();
        String position = requestDto.getPosition();

        UserRegisterResponseDto responseDto = new UserRegisterResponseDto();

        if (email == null || password == null || empName == null
            || email.length() == 0 || password.length() == 0 || empName.length() == 0) {
            throw new Exception403("회원정보가 제대로 입력되지 않았습니다.");
        } else if (userRepository.findByEmail(email).isPresent()) {
            throw new Exception403("이메일이 중복되었습니다.");
        } else {
            try {

                User user = userRepository.save(User.builder()
                    .email(email)
                    .password(passwordEncoder.encode(password))
                    .empName(aes256.encrypt(empName))
                    .position(position)
                    .role(Role.USER)
                    .annualCount(12)
                    .build());

                responseDto.setId(user.getId());
                responseDto.setEmail(user.getEmail());
            } catch (Exception e) {
                e.printStackTrace();
                throw new Exception400("status", e.getMessage());
            }
        }
        return responseDto;
    }

    public UserLoginResponseDto login2(HttpServletRequest request, UserLoginRequestDto requestDto)
        throws Exception {
        log.info("로그인 시도 중");
        log.info("이메일 중복확인");
        if (!userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new Exception401("이메일을 확인해주세요.");
        }
        log.info("비밀번호 확인");
        Optional<User> user = userRepository.findByEmail(requestDto.getEmail());
        if (!passwordEncoder.matches(requestDto.getPassword(), user.get().getPassword())) {
            throw new Exception401("비밀번호를 확인해주세요.");
        }

        String accessToken = jwtTokenProvider.createToken(requestDto.getEmail());
        String refreshToken = jwtTokenProvider.createRefreshToken();

        user.get().updateRefreshToken(refreshToken);

        log.info("유저 정보, 토큰정보");
        UserLoginResponseDto responseDto = UserLoginResponseDto.builder()
            .id(user.get().getId())
            .email(user.get().getEmail())
            .empName(aes256.decrypt(user.get().getEmpName()))
            .empNo(user.get().getEmpNo())
            .position(user.get().getPosition())
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();

        logRepository.save(LoginLog.builder()
            .user(user.get())
            .userAgent(request.getHeader("user-agent"))
            .clientIP(request.getRemoteAddr())
            .build());

        log.info("로그인 서비스단 종료");
        return responseDto;
    }

    @Override
    public Boolean emailExists(String email) {
        if (email == null || email.length() == 0) {
            throw new Exception403("이메일을 정확히 입력해주세요.");
        } else if (userRepository.findByEmail(email).isPresent()) {
            throw new Exception403("이미 사용중인 이메일 입니다.");
        } else {
            return false;
        }
    }

    @Override
    public TokenDto reIssue(TokenDto requestDto) {
        log.info("refreshToken 유효기간 체크");
        if (!jwtTokenProvider.validateTokenExceptExpiration(requestDto.getRefreshToken())) {
            throw new Exception401("refreshToken 유효하지 않음");
        }

        log.info("userInfo 불러오기");
        User user = findUserByToken(requestDto);

        log.info("refreshToken 일치 확인");
        if (!user.getRefreshToken().equals(requestDto.getRefreshToken())) {
            throw new Exception401("refreshToken 유효하지 않음");
        }

        String accessToken = jwtTokenProvider.createToken(user.getEmail());
        String refreshToken = jwtTokenProvider.createRefreshToken();
        user.updateRefreshToken(refreshToken);
        log.info("재발급 서비스단 종료");
        return new TokenDto(accessToken, refreshToken);
    }

    @Override
    public User findUserByToken(TokenDto requestDto) {
        Authentication auth = jwtTokenProvider.getAuthentication(requestDto.getAccessToken());
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String username = userDetails.getUsername();
        return userRepository.findByEmail(username).orElseThrow(() -> new Exception401("유저가 존재하지 않음"));
    }

}
