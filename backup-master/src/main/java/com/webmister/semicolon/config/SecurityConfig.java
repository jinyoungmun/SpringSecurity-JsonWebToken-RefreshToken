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
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                .antMatchers("/api/signUp").permitAll()
                .antMatchers("/api/login").permitAll()
                .antMatchers("/api/get").permitAll()
                .antMatchers("/api/refresh/{userNickname}").permitAll()
                .antMatchers("/api/signup/{userNickname}/{authKey}").permitAll()
                .antMatchers("/api/printAll").access("hasAnyRole('ADMIN')")
                .antMatchers("/api/{userNickname}").access("hasAnyRole('USER','ADMIN')")
                .antMatchers("/api/userDelete/{userNickname}").access("hasAnyRole('USER','ADMIN')")

                .antMatchers("/api/{departStatus}/{userNickname}/reportUpload").access("hasAnyRole('USER','ADMIN')")
                .antMatchers("/api/{userNickname}/{reportId}").access("hasAnyRole('USER','ADMIN')")
                .antMatchers("/api/{departStatus}/showAll").access("hasAnyRole('USER', 'ADMIN')")
                .antMatchers("/api/{userNickname}/reportDelete").access("hasAnyRole('USER','ADMIN')")
                .antMatchers("/api/{departStatus}/showAll").permitAll()

                .antMatchers("/api/{userNickname}/profileImageUp").access("hasAnyRole('USER','ADMIN')")

                .antMatchers("/api/friend/friendMatch/{postFriendNickname}").access("hasAnyRole('USER','ADMIN')")
                .antMatchers("/api/friend/printAll/{userNickName}").access("hasAnyRole('USER','ADMIN')")
                .antMatchers("/api/friend/friendDelete/{postFriendNickname}").access("hasAnyRole('USER','ADMIN')")

                .antMatchers("/api/commentUpload").access("hasAnyRole('USER','ADMIN')")


                .anyRequest().authenticated()

                .and()
                .apply(new JwtSecurityConfig(jwtTokenProvider));

        return http.build();
    }
}