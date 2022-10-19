package com.webmister.semicolon.service;

import com.webmister.semicolon.domain.FriendMatch;
import com.webmister.semicolon.domain.UserInfo;
import com.webmister.semicolon.enumclass.FriendStatus;
import com.webmister.semicolon.repository.FriendMatchRepository;
import com.webmister.semicolon.repository.UserInfoRepository;
import com.webmister.semicolon.request.FriendMatchRequest;
import com.webmister.semicolon.response.FriendMatchResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class FriendMatchService {
    final FriendMatchRepository friendMatchRepository;

    final UserInfoRepository userInfoRepository;

    public FriendMatchService(FriendMatchRepository friendMatchRepository, UserInfoRepository userInfoRepository)  {
        this.friendMatchRepository = friendMatchRepository;
        this.userInfoRepository = userInfoRepository;
    }

    public FriendMatchResponse friendMatchResponseSave(FriendMatchResponse friendMatchResponse, String postFriendNickname, String receiveFriendNickname, FriendStatus friendStatus){
        friendMatchResponse.setPostFriendNickname(postFriendNickname);
        friendMatchResponse.setReceiveFriendNickname(receiveFriendNickname);
        friendMatchResponse.setFriendStatus(friendStatus);
        return friendMatchResponse;
    }

    public FriendMatchResponse FriendMatchSave(String userInfoNickname, FriendMatchRequest friendMatchRequest){
            UserInfo postFriend = userInfoRepository.findUserInfoByUserNickName(userInfoNickname);
            UserInfo receiveFriend = userInfoRepository.findUserInfoByUserNickName(friendMatchRequest.getReceiveFriendNickname());
            FriendStatus friendStatus = friendMatchRequest.getFriendStatus();
            FriendMatchResponse friendMatchResponse = new FriendMatchResponse();
            try {
                friendMatchRepository.save(FriendMatch.builder()
                        .postFriendId(postFriend)
                        .receiveFriendId(receiveFriend)
                        .friendStatus(friendStatus)
                        .build());
                friendMatchResponseSave(friendMatchResponse, userInfoNickname, receiveFriend.getUserNickName(), friendStatus);
            }catch (Exception e){
                return new FriendMatchResponse();
            }
            return friendMatchResponse;
    }

    public Boolean FriendMatchDelete(String userInfoNickname, FriendMatchRequest friendMatchRequest){
        UserInfo postFriend = userInfoRepository.findUserInfoByUserNickName(userInfoNickname);
        UserInfo receiveFriend = userInfoRepository.findUserInfoByUserNickName(friendMatchRequest.getReceiveFriendNickname());
        FriendStatus friendStatus = friendMatchRequest.getFriendStatus();
        try {
            if (friendStatus == FriendStatus.UNFOLLOW) {
                Long unFollow = friendMatchRepository.findFriendMatchByPostFriendIdAndReceiveFriendId(postFriend, receiveFriend).getFriendMatchId();
                friendMatchRepository.deleteById(unFollow);
            }
        }catch (Exception e){
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public List<FriendMatch> FriendList(String userNickname){
        List<FriendMatch> friendMatchList = userInfoRepository.findUserInfoByUserNickName(userNickname).getFriendMatchList();
        return friendMatchList;
    }

//    public List<FriendMatch> FriendProfileList(String userNickname){
//        List<FriendMatch> friendProfileList = userInfoRepository.findUserInfoByUserNickName(userNickname).getFriendMatchList();
//        return  friendProfileList
//    }


}