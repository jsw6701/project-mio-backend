package com.gdsc.projectmiobackend.controller;


import com.gdsc.projectmiobackend.dto.CommentDto;
import com.gdsc.projectmiobackend.dto.request.CommentFirstCreateRequestDto;
import com.gdsc.projectmiobackend.dto.request.CommentPatchRequestDto;
import com.gdsc.projectmiobackend.dto.request.CommentRequestDto;
import com.gdsc.projectmiobackend.entity.Comment;
import com.gdsc.projectmiobackend.jwt.dto.UserInfo;
import com.gdsc.projectmiobackend.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@Tag(name = "댓글")
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "부모 댓글 생성")
    @PostMapping("/{postId}")
    public ResponseEntity<CommentDto> createComment(@RequestBody CommentFirstCreateRequestDto commentRequestDto, @PathVariable Long postId, @AuthenticationPrincipal UserInfo user){

        Comment comment;

        comment = commentService.addFirstComment(commentRequestDto, postId, user.getEmail());

        return ResponseEntity.ok(comment.toDto());
    }

    @Operation(summary = "자식 댓글 생성")
    @PostMapping("/{parentId}")
    public ResponseEntity<CommentDto> createReComment(@RequestBody CommentRequestDto commentRequestDto, @PathVariable Long parentId, @AuthenticationPrincipal UserInfo user){

        Comment comment;

        comment = commentService.addChildComment(commentRequestDto, parentId, user.getEmail());

        return ResponseEntity.ok(comment.toDto());
    }


    @Operation(summary = "댓글 조회(근데 자식 댓글이 부모 댓글쪽에서 자식 리스트로 나오고 밖에서 또 나옴")
    @GetMapping("/{postId}")
    public ResponseEntity<List<CommentDto>> readAllByPostId(@PathVariable Long postId){
        List<CommentDto> commentDtoList = this.commentService.getCommentList(postId);
        return ResponseEntity.ok(commentDtoList);
    }

    @Operation(summary = "부모 댓글 조회(이건 자식 댓글이 부모 댓글의 자식 리스트에서만 출력되게 함")
    @GetMapping("/parent/{postId}")
    public ResponseEntity<List<CommentDto>> getParentCommentsByPostId(@PathVariable Long postId) {
        List<CommentDto> commentList = commentService.getParentCommentList(postId);
        return ResponseEntity.ok(commentList);
    }

    @Operation(summary = "부모 ID로 자식 댓글 조회")
    @GetMapping("/child/{parentId}")
    public ResponseEntity<List<CommentDto>> getChildCommentsByParentId(@PathVariable Long parentId) {
        List<CommentDto> commentList = commentService.getChildCommentList(parentId);
        return ResponseEntity.ok(commentList);
    }


    @Operation(summary = "댓글 수정")
    @PatchMapping("{commentId}")
    public ResponseEntity<CommentDto> update(@RequestBody CommentPatchRequestDto commentPatchRequestDto, @PathVariable Long commentId, @AuthenticationPrincipal UserInfo user){
        Comment comment = this.commentService.updateComment(commentPatchRequestDto, commentId, user.getEmail());
        return ResponseEntity.ok(comment.toDto());
    }

    @Operation(summary = "댓글 삭제")
    @PatchMapping("/delete/{commentId}")
    public ResponseEntity<CommentDto> deleteComment(@PathVariable Long commentId, @AuthenticationPrincipal UserInfo user){
        Comment comment = this.commentService.deleteComment(commentId, user.getEmail());
        return ResponseEntity.ok(comment.toDto());
    }
}
