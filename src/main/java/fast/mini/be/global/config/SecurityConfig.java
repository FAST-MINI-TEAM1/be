package fast.mini.be.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import fast.mini.be.domain.user.repository.UserRepository;
import fast.mini.be.domain.user.service.LoginService;
import fast.mini.be.global.jwt.filter.JwtAuthenticationProcessingFilter;
import fast.mini.be.global.jwt.service.JwtService;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final LoginService loginService;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
    private final JwtService jwtService;

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
                .antMatchers("/api/user/**")
                .access("hasRole('ADMIN') or hasRole('USER')")
                .antMatchers("/api/admin/**")
                .access("hasRole('ADMIN')")
                .anyRequest().authenticated()
                .and()
                .cors().configurationSource(configurationSource());

        http.addFilterAfter(jsonEmailPasswordLoginFilter(), LogoutFilter.class);
        http.addFilterBefore(jwtAuthenticationProcessingFilter(),
                JsonEmailPasswordAuthenticationFilter.class);

//            .and()
//            .addFilterBefore(jwtAuthenticationFilter(jwt, tokenService), UsernamePasswordAuthenticationFilter.class)

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(loginService);
        return new ProviderManager(provider);
    }

    @Bean
    public LoginSuccessJWTProvideHandler loginSuccessJWTProvideHandler() {
        return new LoginSuccessJWTProvideHandler(jwtService, userRepository);//변경
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


    @Bean
    public JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter() {
        JwtAuthenticationProcessingFilter jsonUsernamePasswordLoginFilter = new JwtAuthenticationProcessingFilter(
                jwtService, userRepository);

        return jsonUsernamePasswordLoginFilter;
    }

    private CorsConfigurationSource configurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedOriginPattern("*");
        configuration.setAllowCredentials(true);
        configuration.addExposedHeader("Authorization");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }


}
