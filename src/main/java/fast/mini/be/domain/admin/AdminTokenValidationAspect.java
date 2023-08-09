package fast.mini.be.domain.admin;

import fast.mini.be.global.erros.exception.Exception401;
import fast.mini.be.global.jwt2.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Aspect
@Component
public class AdminTokenValidationAspect {
    private final JwtTokenProvider jwtTokenProvider;

    @Before("execution(* fast.mini.be.domain.admin.AdminController.*(..)) && args(token,..)")
    public void validateToken(String token) throws Exception401 {
//        String jwtToken = token.replace("Bearer ", "");
        jwtTokenProvider.getMemberEmail(token);
//            .orElseThrow(() -> new Exception401("유효하지 않은 토큰입니다"));
    }
}
