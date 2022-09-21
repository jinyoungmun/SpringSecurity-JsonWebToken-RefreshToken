package com.webmister.semicolon.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/*
    * 처음 refreshToken 어디서 어떻게 저장하는지 파악. <- 이걸 하면 4번째줄 일을 해결할 수 있음. <- 여기까지 오늘 자습시간에
    *. 핵심은 userInfo db에 refreshToken 컬럼을 만들어서 할지, ManyToMany 유지할지, OneToMany 할지 생각. (결론은 컬럼으로)
    3. JwtTokenProvider return null 수정.
    * UserInfoService 로그인을 하면 db에 refreshToken 저장- 하는데 signUp 에서 냅두고 service login 메서드에서 하거나 provider
    createToken 에서 refreshToken.save 하거나.
    5. RefreshController RefreshApiResponseMessage
    * JwtService login 메서드 UserInfoController 에 적용시켜서 (구) 리프레시 토큰이 있으면 삭제하도록.
    - 가장 큰 난관은 JwtService login 메서드에서의 existsBy를 어떻게 할 것인가 -

    - 2 리프레시 토큰에 id를 따로 만들면 나는 로그인을 할때 리프레시토큰을 저장하게 되니까
    두 회원이 회원가입을 했는데, 늦게 회갑한 사람이 먼저 로그인해서 리프레시 토큰을 받게 되면 id의 값과 refreshToken 값이 서로 달라지니까.
 */

@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        source.registerCorsConfiguration("/api/**", config);
        return new CorsFilter(source);
    }
}