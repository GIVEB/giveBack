package ten.give.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ten.give.web.service.LoginService;

/**
 * security 를 적용하면 spring 은 default 로 모든 API 에 인증이 필요한 것으로 설정한다.
 */

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AuthenticationConfig {

    private final LoginService loginService;

    /**
     * properties 내 설정
     */
    @Value("${jwt.secret}")
    private String secretKey;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                .httpBasic().disable() // 인증을 UI 가 아닌 Token 인증
                .csrf().disable() // cross site
                .cors().and()
                .authorizeHttpRequests() //request 인가
                .antMatchers("/api/donorcards/users/login", "/api/users/join", "/api/users/sendsms","/api/users/findemail","/api/users/findpassword","email/**").permitAll() // join , login 은 언제나 사용 가능 (인가 필요 없음)
                .antMatchers(HttpMethod.POST,"/api/donorcards/list","/api/users/editPassword").authenticated() //API 의 post 요청을 인증 필요
                .antMatchers(HttpMethod.DELETE, "/api/donorcards/list").authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) //jwt 사용하는 경우
                .and()
                // JwtFilter 인증 계층
                .addFilterBefore(new JwtFilter(loginService,secretKey), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}
