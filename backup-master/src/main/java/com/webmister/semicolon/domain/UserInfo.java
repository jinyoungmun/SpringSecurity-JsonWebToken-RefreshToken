package com.webmister.semicolon.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.webmister.semicolon.enumclass.UserStatus;
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
    Long userInfoId;

    @Column(nullable = false)
    private String userEmail;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String decodedPassword;

    @Column(nullable = false)
    private String userNickName;

    @Column(nullable = false)
    private LocalDateTime userInfoCreateDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus userUniqueID;

    @Column
    private String userProfileImageUrl;

    @Column
    private String userDescription;

    @Column
    private boolean activated;

    @Column
    private String refreshToken;

    @Column
    private String userEmailAuthKey;

    @Column
    private Boolean userEmailAuthState;


    @OneToMany(mappedBy = "userInfo", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<Report> reportList = new ArrayList<Report>();

    @Column
    @OneToMany(mappedBy = "userInfo", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Comment> commentList = new ArrayList<Comment>();

    @Column
    @OneToMany(mappedBy = "postFriendId", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<FriendMatch> friendMatchList = new ArrayList<FriendMatch>();


    public UserInfo setPassword(String password) {
        this.password = password;
        return this;
    }

    public UserInfo setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public UserInfo setAuthorities(Set authorities){
        this.authorities = authorities;
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