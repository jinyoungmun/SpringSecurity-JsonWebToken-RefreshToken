package com.webmister.semicolon.service;

import com.webmister.semicolon.domain.Authority;
import com.webmister.semicolon.domain.UserInfo;
import com.webmister.semicolon.dto.TokenDto;
import com.webmister.semicolon.repository.AuthorityRepository;
import com.webmister.semicolon.repository.RefreshTokenRepository;
import com.webmister.semicolon.repository.UserInfoRepository;
import com.webmister.semicolon.request.Login;
import com.webmister.semicolon.request.TokenRequest;
import com.webmister.semicolon.request.UserInfoRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.security.core.token.Token;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class UserInfoService {
    private final UserInfoRepository userInfoRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;

    public UserInfoService
            (
                    UserInfoRepository userInfoRepository,
                    RefreshTokenRepository refreshTokenRepository,
                    AuthorityRepository authorityRepository,
                    PasswordEncoder passwordEncoder
            )
    {
        this.userInfoRepository = userInfoRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserInfo findUserInfoById(Long id){
        return userInfoRepository.findById(id).orElse(new UserInfo());
    }

    public UserInfo findUserInfoByUserNickname(String userNickname){
        return userInfoRepository.findUserInfoByUserNickName(userNickname).orElse(new UserInfo());
    }

    public List<UserInfo> findAll(){
        List<UserInfo> AllUser = userInfoRepository.findAll();
        System.out.println(AllUser);
        return AllUser;
    }

    public boolean checkDuplicateEmail(String userEmail) {
        return userInfoRepository.existsByUserEmail(userEmail);
    }

    public boolean checkDuplicateUserNickname(String userNickname) {
        return userInfoRepository.existsByUserNickName(userNickname);
    }

    public UserInfo login(Login login) {
        log.info("서비스 로그인");
//        UserInfoRequest userInfoRequest = new UserInfoRequest();
//
//
//        login1(userInfoRequest);
        return userInfoRepository.findByUserEmailAndPassword(login.getUserEmail(), login.getPassword())
                .orElse(new UserInfo());
    }

    public Boolean login1(TokenDto tokenDto, UserInfoRequest userInfoRequest) {

        //UserInfoRequest userInfoRequest = new UserInfoRequest();

//        userInfoRequest.setRefreshToken(tokenDto.getRefreshToken());
//        log.info(userInfoRequest.getRefreshToken());
        String a = tokenDto.getRefreshToken();
        userInfoRequest.setRefreshToken(a);
        log.info(a);
        log.info(userInfoRequest.getRefreshToken());

        try {
            userInfoRepository.save(UserInfo.builder()
                    .refreshToken(userInfoRequest.setRefreshToken(tokenDto.getRefreshToken()))
                    .refreshToken(userInfoRequest.getRefreshToken())
                    .build());

            log.info("리프레시 저장");
            return Boolean.TRUE;

        } catch (Exception e) {
            log.info("리프레시 실패");
            log.info(tokenDto.getRefreshToken());
            return Boolean.FALSE;
        }
    }

    public UserInfo updatePasswordService(String email, String password) {
        UserInfo userInfo = userInfoRepository.findByUserEmail(email)
                .orElse(new UserInfo());
        userInfoRepository.save(userInfo.setPassword(password));
        return userInfo;
    }

    public Boolean signUp(UserInfoRequest userInfoRequest) {

        try {

            userInfoRepository.save(UserInfo.builder()
                    .password(passwordEncoder.encode(userInfoRequest.getPassword()))
                    .userEmail(userInfoRequest.getUserEmail())
                    .userNickName(userInfoRequest.getUserNickName())
                    .userUniqueID(userInfoRequest.getUserUniqueID())
                    .userProfileImageUrl(userInfoRequest.getUserProfileImageUrl())
                    .userDescription(userInfoRequest.getUserDescription())
                    .authorities(Collections.singleton(authorityRepository.save(Authority.builder().authorityName("ROLE_USER").build())))
                    .activated(true)
                    .build());

            log.info("서비스 회갑");
            return Boolean.TRUE;
        } catch (Exception e) {
            log.info("서비스 회갑 실패");
            return Boolean.FALSE;
        }
    }

//    public EssentialUserInfo getUserWithAuthorities(String userEmail) {
//        return EssentialUserInfo.from(userInfoRepository.findOneWithAuthoritiesByUserEmail(userEmail).orElse(null));
//    }
//
//    //@Transactional(readOnly = true)
//    public EssentialUserInfo getMyUserWithAuthorities() {
//        return EssentialUserInfo.from(
//                SecurityUtil.getCurrentUsername()
//                        .flatMap(userInfoRepository::findOneWithAuthoritiesByUserEmail)
//                        .orElseThrow(() -> new NotFoundMemberException("Member not found"))
//        );
//    }
}
