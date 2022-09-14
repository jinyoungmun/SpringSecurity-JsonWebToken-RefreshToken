package com.webmister.semicolon.domain;


import lombok.*;

import javax.persistence.*;

@Entity
@Data
@Table(name = "refresh")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {
    @Id
    @Column(name = "refreshToken")
    public String refreshToken;
}
