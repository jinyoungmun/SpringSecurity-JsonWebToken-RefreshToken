package com.webmister.semicolon.jwt;

import com.webmister.semicolon.domain.Authority;
import com.webmister.semicolon.domain.UserInfo;
import com.webmister.semicolon.dto.TokenDto;
import com.webmister.semicolon.repository.AuthorityRepository;
import com.webmister.semicolon.repository.UserInfoRepository;
import com.webmister.semicolon.request.UserInfoRequest;
import com.webmister.semicolon.service.UserInfoService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.token.Token;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider implements InitializingBean {

    private static final String AUTHORITIES_KEY = "auth";
    private final String secret;
    private final long accessTokenValidityInMilliseconds;
    private final long refreshTokenValidityInMilliseconds;
    private final UserInfoRepository userInfoRepository;
    private final AuthorityRepository authorityRepository;
    private Key key;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-validity-in-seconds}") long accessTokenValidityInSeconds,
            @Value("${jwt.refresh-token-validity-in-seconds}") long refreshTokenValidityInSeconds,
            UserInfoRepository userInfoRepository,
            AuthorityRepository authorityRepository

    ){
        this.secret = secret;
        this.accessTokenValidityInMilliseconds = accessTokenValidityInSeconds * 1000;
        this.refreshTokenValidityInMilliseconds = refreshTokenValidityInSeconds * 1000;
        this.userInfoRepository = userInfoRepository;
        this.authorityRepository = authorityRepository;
    }

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenDto createToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Date now = new Date();

        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(new Date(now.getTime() +accessTokenValidityInMilliseconds))
                .compact();

        String refreshToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(new Date(now.getTime() +refreshTokenValidityInMilliseconds))
                .compact();

        return TokenDto.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }

    // JWT 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

//    public boolean validateRefreshToken(RefreshToken refreshTokenObj){
//
//        String refreshToken = refreshTokenObj.getRefreshToken();
//
//        try{
//            Jws.parserBuilder().setSigningKey(key).build().parseClaimsJws(refreshToken);
//            if(!Jws.claims().getExpiration().before(new Date())){
//                return reCreateToken();
//            }
//            return true;
//        }
//        catch(SignatureException | MalformedJwtException e){
//            // 서명 오류, JWT 구조 문제
//            log.info("");
//        }
//        catch (ExpiredJwtException e){
//            log.info("재로그인 필요");
//        }
//        catch (Exception e){
//            // 그 이외의 오류
//        }
//        return false; // false면 401에러.
//    }

    public Boolean validateRefreshToken(String refreshToken){

        //String refreshToken = refreshTokenObj.getRefreshToken();

        try{
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(refreshToken);
            // refresh 토큰 만료가 안됬으면 새로운 access 토큰 생성.
            if(!claims.getBody().getExpiration().before(new Date())){
            log.info("액세스 토큰 재생성");
            }
            return Boolean.TRUE;
        }
        catch (Exception e){
            // refresh 토큰이 만료된 경우, 로그인이 필요.
            log.info("재로그인 필요");
            return Boolean.FALSE;
        }
    }

    //refresh 유효성 검사 거치고 accessToken 새로 발급하는 메서드.
    public TokenDto reCreateToken(Authentication authentication) {

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date validity = new Date(now + this.accessTokenValidityInMilliseconds);

        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();

        return TokenDto.builder().accessToken(accessToken).build();
    }
}
