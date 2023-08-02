package fast.mini.be.domain.admin;

import fast.mini.be.global.erros.exception.Exception401;
import fast.mini.be.global.jwt.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Aspect
@Component
public class AdminTokenValidationAspect {
    private final JwtService jwtService;

    @Before("execution(* fast.mini.be.domain.admin.AdminController.*(..)) && args(token,..)")
    public void validateToken(String token) throws Exception401 {
        String jwtToken = token.replace("Bearer ", "");
        jwtService.extractUsername(jwtToken).orElseThrow(() -> new Exception401("유효하지 않은 토큰입니다"));
    }
}
