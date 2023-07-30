package fast.mini.be.global.jwt.filter;


import fast.mini.be.domain.user.repository.UserRepository;
import fast.mini.be.global.jwt.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    private final String NO_CHECK_URL = "/api/login";

    /**
     * 1. 리프레시 토큰이 오는 경우 -> 유효하면 AccessToken 재발급후, 필터 진행 X, 바로 튕기기
     * <p>
     * 2. 리프레시 토큰은 없고 AccessToken만 있는 경우 -> 유저정보 저장후 필터 계속 진행
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURI().equals(NO_CHECK_URL)) {
            filterChain.doFilter(request, response);
            return;//안해주면 아래로 내려가서 계속 필터를 진행해버림
        }

        String refreshToken = jwtService
            .extractRefreshToken(request)
            .filter(jwtService::isTokenValid)
            .orElse(null); //2

        if (refreshToken != null) {
            checkRefreshTokenAndReIssueAccessToken(response, refreshToken);//3
            return;
        }

        checkAccessTokenAndAuthentication(request, response, filterChain);//4

    }


    private void checkAccessTokenAndAuthentication(HttpServletRequest request,
        HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        jwtService.extractAccessToken(request).filter(jwtService::isTokenValid).ifPresent(

            accessToken -> jwtService.extractUsername(accessToken).ifPresent(

                email -> userRepository.findByEmail(email).ifPresent(

                    this::saveAuthentication
                )
            )
        );

        filterChain.doFilter(request, response);
    }


    private void saveAuthentication(fast.mini.be.domain.user.User domainUser) {
        UserDetails user = User.builder()
            .username(domainUser.getEmail())
            .password(domainUser.getPassword())
            .roles(domainUser.getRole().name())
            .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null,
            authoritiesMapper.mapAuthorities(user.getAuthorities()));

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    private void checkRefreshTokenAndReIssueAccessToken(HttpServletResponse response,
        String refreshToken) {

        userRepository.findByRefreshToken(refreshToken).ifPresent(
            user -> jwtService.sendAccessToken(response,
                jwtService.createAccessToken(user.getEmail()))
        );


    }
}
