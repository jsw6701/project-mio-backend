package com.gdsc.projectmiobackend.controller;


import com.gdsc.projectmiobackend.dto.CommentDto;
import com.gdsc.projectmiobackend.dto.request.CategoryPatchRequestDto;
import com.gdsc.projectmiobackend.dto.request.CommentPatchRequestDto;
import com.gdsc.projectmiobackend.dto.request.CommentRequestDto;
import com.gdsc.projectmiobackend.entity.Comment;
import com.gdsc.projectmiobackend.jwt.dto.UserInfo;
import com.gdsc.projectmiobackend.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;

    @PostMapping("{postId}")
    public ResponseEntity<CommentDto> create(@RequestBody CommentRequestDto commentRequestDto, @PathVariable Long postId, @AuthenticationPrincipal UserInfo user){
        Comment comment = this.commentService.addComment(commentRequestDto, postId, user.getEmail());
        return ResponseEntity.ok(comment.toDto());
    }

    @GetMapping("{postId}")
    public ResponseEntity<List<CommentDto>> readAllByPostId(@PathVariable Long postId){
        List<CommentDto> commentDtoList = this.commentService.getCommentList(postId);
        return ResponseEntity.ok(commentDtoList);
    }

    @PatchMapping("{commentId}")
    public ResponseEntity<CommentDto> update(@RequestBody CommentPatchRequestDto commentPatchRequestDto, @PathVariable Long commentId, @AuthenticationPrincipal UserInfo user){
        Comment comment = this.commentService.updateComment(commentPatchRequestDto, commentId, user.getEmail());
        return ResponseEntity.ok(comment.toDto());
    }
}
