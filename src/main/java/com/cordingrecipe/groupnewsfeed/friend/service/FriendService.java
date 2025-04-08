package com.cordingrecipe.groupnewsfeed.friend.service;


import com.cordingrecipe.groupnewsfeed.friend.entity.Friends;
import com.cordingrecipe.groupnewsfeed.friend.repository.FriendRepository;
import com.cordingrecipe.groupnewsfeed.friend.dto.CreateFriendRequestDto;
import com.cordingrecipe.groupnewsfeed.friend.dto.CreateFriendResponseDto;
import com.cordingrecipe.groupnewsfeed.user.entity.User;
import com.cordingrecipe.groupnewsfeed.user.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    //친구 요청

    //ManyToOne관계는 객체 User를 직접 넣어줘야함. 객체 간의 연관관계를 이용해서 자동으로 관리해주기 때문에
    //User엔티티 객체가 필요하며, DB에서 실제 User엔티티를 가져와야 JPA가 내부적으로 user_id외래키 자동 처리오

    public CreateFriendResponseDto createFriend(CreateFriendRequestDto createFriendRequestDto, HttpSession session) {
        //로그인 유저 정보 조회 세션
        //세션에서 내 id가ㅏ 등록되어있는지 확인 그냥 숫자 조회.
        Long userId = (Long) session.getAttribute("LOGIN_USER");
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }
        //DB에 유저 엔티티가 존재하는지.
        User loginUser = userRepository.findById(userId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 id입니다."));

        Long receiverId = createFriendRequestDto.getReceiverId();
        //receiverId검증.
        User toUser = userRepository.findById(receiverId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다."));

        //친구 관계, 요청한 이력있는지 중복검사
        boolean alredyRequested = friendRepository.existsByFromUserAndToUser(loginUser, toUser);
        if (alredyRequested) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 친구 요청을 보냈습니다.");
        }
        //친구 요청 객체 생성 PENDING상태
        Friends friend = new Friends();
        friend.setFromUser(loginUser);
        friend.setToUser(toUser);
        friend.setStatus(Friends.FriendRequestStatus.PENDING);

        //응답 dto
        return new CreateFriendResponseDto(friend);

    }
    //친구 요청 수락
    public CreateFriendResponseDto acceptFriendRequest(Long requesterId, HttpSession session) {
        Long userId = (Long) session.getAttribute("LOGIN_USER");
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }
        //DB에 유저 엔티티가 존재하는지.
        User loginUser = userRepository.findById(userId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 id입니다."));

        //친구 요청이 존재하지 않습니다.
        Friends friendRequest = friendRepository.findById(requesterId).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 친구요청입니다."));

        //요청 수락 권한 없음
        if(!friendRequest.getToUser().equals(loginUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"요청 수락 권한이 없습니다.");
        }

        // 친구 요청 수락처리.
        friendRequest.setStatus(Friends.FriendRequestStatus.ACCEPTED);
        friendRepository.save(friendRequest);

        return new CreateFriendResponseDto(friendRequest);
    }
    //친구 요청 거절
    public CreateFriendResponseDto rejectFriendRequest(Long requesterId, HttpSession session) {
        Long userId = (Long) session.getAttribute("LOGIN_USER");
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }
        User loginUser = userRepository.findById(userId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 ID입니다."));

        Friends friendRequest = friendRepository.findById(requesterId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,"존재하지 않는 친구요청입니다."));

        if(!friendRequest.getToUser().equals(loginUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"요청 수락 권한이 없습니다.");
        }

        //거절
        friendRequest.setStatus(Friends.FriendRequestStatus.REJECTED);
        friendRepository.save(friendRequest);

        return new CreateFriendResponseDto(friendRequest);


    }
    //내가 받은 친구 목록 조회
    @Transactional
    public void getReceivedRequests(Long requesterId, HttpSession session) {
        Long userId = (Long) session.getAttribute("LOGIN_USER");
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }




    }





    //그냥 친구 목록getMyFriends()

}







//친구 삭제

