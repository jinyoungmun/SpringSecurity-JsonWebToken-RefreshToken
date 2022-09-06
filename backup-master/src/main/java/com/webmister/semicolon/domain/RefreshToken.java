package com.webmister.semicolon.domain;


import lombok.*;

import javax.persistence.*;

@Entity
@Data
@Table(name = "refreshToken")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long refreshTokenId;

    @Column(nullable = false)
    private String refreshToken;
}
