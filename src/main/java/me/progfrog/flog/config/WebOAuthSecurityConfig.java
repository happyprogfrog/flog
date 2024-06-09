package me.progfrog.flog.config;

import lombok.RequiredArgsConstructor;
import me.progfrog.flog.config.jwt.TokenProvider;
import me.progfrog.flog.config.oauth.OAuth2AuthorizationRequestBasedOnCookieRepository;
import me.progfrog.flog.config.oauth.OAuth2SuccessHandler;
import me.progfrog.flog.config.oauth.OAuth2UserCustomService;
import me.progfrog.flog.repository.RefreshTokenRepository;
import me.progfrog.flog.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@RequiredArgsConstructor
public class WebOAuthSecurityConfig {

    private final OAuth2UserCustomService oAuth2UserCustomService;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;

    @Bean
    public WebSecurityCustomizer configure() {
        // 스프링 시큐리티 기능 비활성화
        return (web) -> web.ignoring()
                .requestMatchers("/img/**", "/css/**", "/js/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 1. 토큰 방식으로 인증을 하므로, 기존에 사용하던 폼 로그인, 세션 비활성화
        http.csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable);

        // 2. 헤더를 확인할 커스텀 필터 추가
        http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        // 3. 토큰 재발급 URL 인증 없이 접근 가능하도록 설정
        http.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                authorizationManagerRequestMatcherRegistry
                        .requestMatchers("/api/token").permitAll()
                        .requestMatchers("/login", "/signup", "/user").permitAll()
                        .anyRequest().authenticated());

        http.oauth2Login(oauth2 -> oauth2
                .loginPage("/login")
                .authorizationEndpoint(authorization -> authorization
                        .authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository())) // 4. Authorization 요청과 관련된 상태 저장
                .successHandler(oAuth2SuccessHandler()) // 5. 인증 성공 시 실행할 핸들러
                .userInfoEndpoint(userInfo -> userInfo
                        .userService(oAuth2UserCustomService)));

        http.logout(logout -> logout
                .logoutSuccessUrl("/login"));

        // 6. /api 로 시작하는 url 인 경우 401 상태 코드를 반환하도록 예외 처리
        http.exceptionHandling(exceptionHandling -> exceptionHandling
                .defaultAuthenticationEntryPointFor(
                        new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                        new AntPathRequestMatcher("/api/**")
                ));

        return http.build();
    }

    @Bean
    public OAuth2SuccessHandler oAuth2SuccessHandler() {
        return new OAuth2SuccessHandler(tokenProvider, refreshTokenRepository, oAuth2AuthorizationRequestBasedOnCookieRepository(), userService);
    }

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(tokenProvider);
    }

    @Bean
    public OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository() {
        return new OAuth2AuthorizationRequestBasedOnCookieRepository();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}