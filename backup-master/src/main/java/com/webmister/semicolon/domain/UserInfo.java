package com.webmister.semicolon.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Table(name = "userInfo")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {

    @Id
    @Column(name = "userInfoId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    private String userEmail;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String userNickName;

    @Column(nullable = false)
    private LocalDateTime userInfoCreateDate;

    @Column(nullable = false)
    private String userUniqueID;

    @Column
    private String userProfileImageUrl;

    @Column
    private String userDescription;

    @Column
    private boolean activated;

    @Column
    private String refreshToken;

    @OneToMany(mappedBy = "userInfo", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<Report> reportList = new ArrayList<Report>();

    public UserInfo setPassword(String password) {
        this.password = password;
        return this;
    }

    public UserInfo setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    @PrePersist
    public void UserInfoCreatDate() {
        this.userInfoCreateDate = LocalDateTime.now();
    }

    @ManyToMany
    @JoinTable(
            name = "userAuthority",
            joinColumns = {@JoinColumn(name = "userInfoId", referencedColumnName = "userInfoId")},
            inverseJoinColumns = {@JoinColumn(name = "authorityName", referencedColumnName = "authorityName")})
    private Set<Authority> authorities;
}