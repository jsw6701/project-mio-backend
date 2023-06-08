package com.gdsc.projectmiobackend.service;


import com.gdsc.projectmiobackend.dto.CommentDto;
import com.gdsc.projectmiobackend.dto.request.CommentFirstCreateRequestDto;
import com.gdsc.projectmiobackend.dto.request.CommentPatchRequestDto;
import com.gdsc.projectmiobackend.dto.request.CommentRequestDto;
import com.gdsc.projectmiobackend.entity.Comment;
import java.util.List;

public interface CommentService {

    Comment addFirstComment(CommentFirstCreateRequestDto commentRequestDto, Long postId, String email);

    Comment addChildComment(CommentRequestDto commentRequestDto, Long postId, String email);

    List<CommentDto> getCommentList(Long postId);

    List<CommentDto> getParentCommentList(Long postId);

    List<CommentDto> getChildCommentList(Long parentId);

    Comment updateComment(CommentPatchRequestDto commentRequestDto, Long commentId, String email);

    Comment deleteComment(Long commentId, String email);

}