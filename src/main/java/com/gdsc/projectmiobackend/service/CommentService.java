package com.gdsc.projectmiobackend.service;


import com.gdsc.projectmiobackend.dto.CommentDto;
import com.gdsc.projectmiobackend.dto.request.CommentRequestDto;
import com.gdsc.projectmiobackend.entity.Comment;
import java.util.List;

public interface CommentService {

    Comment addComment(CommentRequestDto commentRequestDto, Long postId, String email);

    List<CommentDto> getCommentList(Long postId );
}
