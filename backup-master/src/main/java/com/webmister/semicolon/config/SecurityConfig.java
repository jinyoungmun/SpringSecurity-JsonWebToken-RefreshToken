/*
1.사용자가 http request.

        2-1.로그인 요청이라면 (UsernamePasswordAuthenticationFilter를 상속한) AuthenticationFilter가
        attemptAuthentication()를 실행되고 그 후 successfulAuthentication()을 실행된다.
        해당 과정에서 토큰을 만들고 해당 토큰을 response 헤더에 담아준다.
        2-2.이미 토큰을 발급받아 같이 요청한 상태라면, (UsernamePasswordAuthenticationFilter를상속한) AuthenticationFilter에서 아무런 동작없이 바로 인가처리로 이어진다.
        (BasicAuthenticationFilter를 상속한 AuthorizationFilter)

        3.BasicAuthenticationFilter를 상속한 AuthorizationFilter에서 받은 토큰을 parse하여 해당 Id가 db에
        저장되어 있는지 확인한다.그후 존재한다면,해당 유저정보와 해당 유저의 권한이 담긴 토큰을
        SecurityContextHolder를 사용하여 세션값에 저장한다.(권한 부여)
        Ex) SecurityContextHolder.getContext().setAuthentication(authentication);

        - @PreAuthorize 말고 securityconfig에서 .access로 대체하는 건지
 */
package com.webmister.semicolon.config;

import com.webmister.semicolon.jwt.JwtSecurityConfig;
import com.webmister.semicolon.jwt.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;
    private final CorsFilter corsFilter;

    public SecurityConfig(
            JwtTokenProvider jwtTokenProvider,
            CorsFilter corsFilter
    ) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.corsFilter = corsFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .formLogin().disable()
                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)

//                .exceptionHandling()
//                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
//                .accessDeniedHandler(jwtAccessDeniedHandler)

//              .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                .antMatchers("/api/signUp").permitAll()
                .antMatchers("/api/login").permitAll()
                .antMatchers("/api/printAll").permitAll()
                .antMatchers("/api/get").permitAll()
                .antMatchers("/api/{userNickname}").access("hasAnyRole('USER','ADMIN')")
                .antMatchers("api/userDelete/{userNickname}").access("hasAnyRole('USER','ADMIN')")

                .antMatchers("/api/{userNickname}/reportUpload").access("hasAnyRole('USER','ADMIN')")
                .antMatchers("/api/{userNickname}/{reportId}").access("hasAnyRole('USER','ADMIN')")
                .antMatchers("/api/{userNickname}/reportDelete").access("hasAnyRole('USER','ADMIN')")

                .antMatchers("/api/{userNickname}/profileImageUp").access("hasAnyRole('USER','ADMIN')")

                .antMatchers("/api/friend/friendMatch/{postFriendNickname}").access("hasAnyRole('USER','ADMIN')")
                .antMatchers("/api/friend/printAll/{userNickname}").access("hasAnyRole('USER','ADMIN')")
                .antMatchers("/api/friend/friendDelete/{postFriendNickname}").access("hasAnyRole('USER','ADMIN')")

                .antMatchers("/api/commentUpload").access("hasAnyRole('USER','ADMIN')")

                .antMatchers("/api/refresh").permitAll()

                .anyRequest().authenticated()

                .and()
                .apply(new JwtSecurityConfig(jwtTokenProvider));

        return http.build();
    }
}