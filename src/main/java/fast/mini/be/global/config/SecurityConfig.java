package fast.mini.be.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import fast.mini.be.domain.user.service.LoginService;
import fast.mini.be.global.login.filter.JsonEmailPasswordAuthenticationFilter;
import fast.mini.be.global.login.handler.LoginFailureHandler;
import fast.mini.be.global.login.handler.LoginSuccessJWTProvideHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final LoginService loginService;
    private final ObjectMapper objectMapper;

    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http, Jwt jwt, TokenService tokenService) throws
    public SecurityFilterChain filterChain(HttpSecurity http) throws
        Exception {
        http
            .formLogin().disable()//1 - formLogin 인증방법 비활성화
            .httpBasic().disable()//2 - httpBasic 인증방법 비활성화(특정 리소스에 접근할 때 username과 password 물어봄)
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

            .and()
            .authorizeRequests()
            .antMatchers("/api/login", "/api/register").permitAll()
            .anyRequest().authenticated();

        http.addFilterAfter(jsonEmailPasswordLoginFilter(), LogoutFilter.class);

//            .and()
//            .addFilterBefore(jwtAuthenticationFilter(jwt, tokenService), UsernamePasswordAuthenticationFilter.class)

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


    @Bean
    public AuthenticationManager authenticationManager() {//2 - AuthenticationManager 등록
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();//DaoAuthenticationProvider 사용
        provider.setPasswordEncoder(
            passwordEncoder());//PasswordEncoder로는 PasswordEncoderFactories.createDelegatingPasswordEncoder() 사용
        //provider.setUserDetailsService(loginService); //이후 작성할 코드입니다.
        return new ProviderManager(provider);
    }

    @Bean
    public LoginSuccessJWTProvideHandler loginSuccessJWTProvideHandler() {
        return new LoginSuccessJWTProvideHandler();
    }

    @Bean
    public LoginFailureHandler loginFailureHandler() {
        return new LoginFailureHandler();
    }

    @Bean
    public JsonEmailPasswordAuthenticationFilter jsonEmailPasswordLoginFilter() {
        JsonEmailPasswordAuthenticationFilter jsonEmailPasswordLoginFilter = new JsonEmailPasswordAuthenticationFilter(
            objectMapper);
        jsonEmailPasswordLoginFilter.setAuthenticationManager(authenticationManager());
        jsonEmailPasswordLoginFilter.setAuthenticationSuccessHandler(
            loginSuccessJWTProvideHandler());
        jsonEmailPasswordLoginFilter.setAuthenticationFailureHandler(loginFailureHandler());
        return jsonEmailPasswordLoginFilter;
    }

}
