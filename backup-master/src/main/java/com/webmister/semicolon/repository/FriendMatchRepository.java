package com.webmister.semicolon.repository;

import com.webmister.semicolon.domain.FriendMatch;
import com.webmister.semicolon.domain.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendMatchRepository extends JpaRepository<FriendMatch, Long> {
    FriendMatch findFriendMatchByPostFriendIdAndReceiveFriendId(UserInfo postFriendId, UserInfo receiveFriendId);

    FriendMatch findByPostFriendId(Long postFriendId);

    List<Long> findAllByPostFriendId(Long postFriendId);

}
