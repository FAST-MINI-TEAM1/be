package fast.mini.be.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

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
            .anyRequest().authenticated()
            ;

//            .and()
//            .addFilterBefore(jwtAuthenticationFilter(jwt, tokenService), UsernamePasswordAuthenticationFilter.class)

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
