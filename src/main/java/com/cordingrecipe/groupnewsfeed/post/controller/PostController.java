package com.cordingrecipe.groupnewsfeed.post.controller;

import com.cordingrecipe.groupnewsfeed.comment.repository.CommentRepository;
import com.cordingrecipe.groupnewsfeed.common.filter.Const;
import com.cordingrecipe.groupnewsfeed.post.dto.request.CreateAndUpdadePostRequestDto;
import com.cordingrecipe.groupnewsfeed.post.dto.response.CreatePostResponseDto;
import com.cordingrecipe.groupnewsfeed.post.dto.response.GetPostWhitCommentDto;
import com.cordingrecipe.groupnewsfeed.post.dto.response.UpdatePostResponseDto;
import com.cordingrecipe.groupnewsfeed.post.service.PostService;
import com.cordingrecipe.groupnewsfeed.user.dto.UserLoginResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor // final 붙은 필드만 모아 자동 생성자 주입
@RestController // @Controller + @ResponseBody
@RequestMapping("/api/boards")
public class PostController {

    // 서비스를 사용하기 위해 만든 필드
    private final PostService postService;
    private final CommentRepository commentRepository;

    @PostMapping
    public ResponseEntity<CreatePostResponseDto> savePostBy(
            @Valid @RequestBody CreateAndUpdadePostRequestDto requestDto,
            @SessionAttribute(Const.LOGIN_USER) UserLoginResponseDto loginUser
    ) {
        Long userId = loginUser.getId(); // 세션에서 로그인된 사용자 ID 꺼내기

        CreatePostResponseDto createPostResponseDto =
                postService.savePostBy(
                        requestDto.getContents(),
                        userId
                );

        return ResponseEntity.ok(createPostResponseDto);
    }

    // 게시글 전체 조회 + 페이징 기능
    @GetMapping("/newsfeed")
    public ResponseEntity<Page<CreatePostResponseDto>> findAllPostBy(
            // @PageableDefault 활용하여 리팩토링
            @PageableDefault(sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<CreatePostResponseDto> allPostPage = postService.findAllPostBy(pageable);

        return ResponseEntity.ok(allPostPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetPostWhitCommentDto> findBy(@PathVariable Long id) {
        GetPostWhitCommentDto getPostWhitCommentDto = postService.findBy(id);

        return ResponseEntity.ok(getPostWhitCommentDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdatePostResponseDto> updatePostBy(
            @PathVariable Long id,
            @Valid @RequestBody CreateAndUpdadePostRequestDto requestDto,
            @SessionAttribute(Const.LOGIN_USER) UserLoginResponseDto loginUser
    ) {
        Long userId = loginUser.getId(); // 세션에서 로그인된 사용자 ID 꺼내기

        UpdatePostResponseDto updatePostResponseDto =
                postService.updatePostBy(id,
                        requestDto.getContents(),
                        userId
                );

        return ResponseEntity.ok(updatePostResponseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBy(
            @PathVariable Long id,
            @SessionAttribute(Const.LOGIN_USER) UserLoginResponseDto loginUser
    ) {
        Long userId = loginUser.getId(); // 세션에서 로그인된 사용자 ID 꺼내기

        postService.deleteBy(id, userId);

        String deleteMessage = "삭제되었습니다";

        return ResponseEntity.ok(deleteMessage);

    }
}
