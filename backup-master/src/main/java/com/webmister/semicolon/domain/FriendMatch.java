package com.webmister.semicolon.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.webmister.semicolon.enumclass.FriendStatus;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FriendMatch {

    @Id
    @Column(name = "friendMatchId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long friendMatchId;

    @ManyToOne
    @JoinColumn(name = "postFriendId")
    @JsonBackReference
    UserInfo postFriendId;

    @ManyToOne
    @JoinColumn(name = "receiveFriendId")
    UserInfo receiveFriendId;

    @Column
    @Enumerated(EnumType.STRING)
    FriendStatus friendStatus = FriendStatus.UNFOLLOW;

}
