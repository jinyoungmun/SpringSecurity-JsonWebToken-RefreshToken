package com.webmister.semicolon.service;

import com.webmister.semicolon.domain.Authority;
import com.webmister.semicolon.domain.UserInfo;
import com.webmister.semicolon.enumclass.UserStatus;
import com.webmister.semicolon.repository.AuthorityRepository;
import com.webmister.semicolon.repository.UserInfoRepository;
import com.webmister.semicolon.request.UserInfoRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Slf4j
@Service
public class JwtService {
    private final UserInfoRepository userInfoRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;


    public JwtService(

            UserInfoRepository userInfoRepository,
            AuthorityRepository authorityRepository,
            PasswordEncoder passwordEncoder
    ){

        this.userInfoRepository = userInfoRepository;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Boolean previousRefreshTokenDelete(String email) {
        UserInfo userInfo = userInfoRepository.findByUserEmail(email).orElse(new UserInfo());

        try {
            if (userInfoRepository.existsByUserEmail(email) == true) {
                log.info("(구) refreshToken 삭제");
                userInfoRepository.save(userInfo.setRefreshToken(null));
                //userInfoRepository.deleteByUserEmail(email);
            }
            return Boolean.TRUE;
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }

    public String findRefreshToken(String email){
        UserInfo userInfo = userInfoRepository.findByUserEmail(email).orElse(new UserInfo());
        return userInfo.getRefreshToken();
    }

    public UserInfoRequest requestSave(UserInfoRequest userInfoRequest, UserInfo userInfo){
        userInfoRequest.setUserEmail(userInfo.getUserEmail());
        userInfoRequest.setDecodedPassword(userInfo.getDecodedPassword());
        userInfoRequest.setUserNickname(userInfo.getUserNickName());
        userInfoRequest.setUserStatus(userInfo.getUserUniqueID());
        userInfoRequest.setUserProfileImageUrl(userInfo.getUserProfileImageUrl());
        userInfoRequest.setUserDescription(userInfo.getUserDescription());
        userInfoRequest.setRefreshToken(userInfo.getRefreshToken());
        log.info("리퀘스트 재저장");
        return userInfoRequest;
    }

    public Boolean roleSetUp(UserInfoRequest userInfoRequest, UserInfo userInfo){
        try{

            userInfoRepository.deleteByUserEmail(userInfo.getUserEmail());
            userInfoRepository.save(UserInfo.builder()
                    .password(passwordEncoder.encode(userInfoRequest.getPassword()))
                    .userEmail(userInfoRequest.getUserEmail())
                    .userNickName(userInfoRequest.getUserNickname())
                    .userUniqueID(UserStatus.USER)
                    .userProfileImageUrl(userInfoRequest.getUserProfileImageUrl())
                    .userDescription(userInfoRequest.getUserDescription())
                    .refreshToken(userInfoRequest.getRefreshToken())
                    .authorities(Collections.singleton(authorityRepository.save(Authority.builder().authorityName("ROLE_USER").build())))
                    .activated(true)
                    .build());

            return Boolean.TRUE;
        }
        catch(Exception e){


            return Boolean.FALSE;
        }
    }

    public Optional<UserInfo> getRefreshToken(String refreshToken){

        return userInfoRepository.findByRefreshToken(refreshToken);
    }
}
