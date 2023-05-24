package com.gdsc.projectmiobackend.service;

import com.gdsc.projectmiobackend.dto.CommentDto;
import com.gdsc.projectmiobackend.dto.request.CommentPatchRequestDto;
import com.gdsc.projectmiobackend.dto.request.CommentRequestDto;
import com.gdsc.projectmiobackend.entity.Comment;
import com.gdsc.projectmiobackend.entity.Post;
import com.gdsc.projectmiobackend.entity.UserEntity;
import com.gdsc.projectmiobackend.repository.CommentRepository;
import com.gdsc.projectmiobackend.repository.PostRepository;
import com.gdsc.projectmiobackend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService{

    private CommentRepository commentRepository;

    private PostRepository postRepository;

    private UserRepository userRepository;

    @Override
    public Comment addComment(CommentRequestDto commentRequestDto, Long postId, String email) {
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("유저정보가 없습니다."));
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("댓글 생성 실패"));

        return commentRepository.save(commentRequestDto.toEntity(post,user));
    }

    @Override
    public List<CommentDto> getCommentList(Long postId) {
        List<Comment> commentList = commentRepository.findByPostId(postId);
        return commentList.stream().map(Comment::toDto).collect(Collectors.toList());
    }

    @Override
    public Comment updateComment(CommentPatchRequestDto commentPatchRequestDto, Long commentId, String email) {
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("유저정보가 없습니다."));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("찾으시는 댓글이 존재하지 않습니다."));
        if (!comment.getUser().getEmail().equals(email)) {
            throw new IllegalStateException("해당 댓글을 수정할 권한이 없습니다.");
        }
        comment.setContent(commentPatchRequestDto.getContent());
        return commentRepository.save(comment);
    }
}
