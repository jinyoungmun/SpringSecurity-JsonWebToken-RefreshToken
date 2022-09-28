package com.webmister.semicolon.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;

@Entity
@Data
@Table(name = "authority")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Authority {
    @Id
    @Column(name ="authorityName")
    public String authorityName;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "userInfoNickName")
//    @JsonBackReference
//    private UserInfo userInfo;

    public Authority setAuthorityName(String authorityName){
        this.authorityName = authorityName;
        return this;
    }
}
