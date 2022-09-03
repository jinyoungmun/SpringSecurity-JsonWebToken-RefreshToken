package com.webmister.semicolon.service;

import com.webmister.semicolon.domain.UserInfo;
import com.webmister.semicolon.repository.UserInfoRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component("userInfoDetailsService")
public class CustomUserInfoDetailsService implements UserDetailsService {
    private final UserInfoRepository userInfoRepository;

    public CustomUserInfoDetailsService(UserInfoRepository userInfoRepository) {
        this.userInfoRepository = userInfoRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String userEmail) {
        return userInfoRepository.findOneWithAuthoritiesByUserEmail(userEmail)
                .map(userInfo -> createUser(userEmail, userInfo))
                .orElseThrow(() -> new UsernameNotFoundException(userEmail + " -> 데이터베이스에서 찾을 수 없습니다."));
    }

    private org.springframework.security.core.userdetails.User createUser(String userEmail, UserInfo userInfo) {
        if (!userInfo.isActivated()) {
            throw new RuntimeException(userEmail + " -> 활성화되어 있지 않습니다.");
        }

        List<GrantedAuthority> grantedAuthorities = userInfo.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName()))
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(userInfo.getUserEmail(),
                userInfo.getPassword(),
                grantedAuthorities);
    }
}