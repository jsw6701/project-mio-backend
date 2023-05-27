package com.gdsc.projectmiobackend.service;


import com.gdsc.projectmiobackend.dto.PostDto;
import com.gdsc.projectmiobackend.dto.request.PostCreateRequestDto;
import com.gdsc.projectmiobackend.dto.request.PostPatchRequestDto;
import com.gdsc.projectmiobackend.entity.Post;
import com.gdsc.projectmiobackend.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface PostService {

    Post findById(Long id);

    Post addPostList(PostCreateRequestDto postCreateRequestDto, Long categoryId, String email) throws IOException;

    void deletePostList(Long id, String email);

    Post updateById(Long id, PostPatchRequestDto postPatchRequestDto, String email);

    Page<PostDto> findPostList(Pageable pageable);

    Page<PostDto> findByCategoryId(Long categoryId, Pageable pageable);

    Page<PostDto> findByMemberId(Long userId, Pageable pageable);

    Post showDetailPost(Long id);

    void participateInPost(Long postId, String email);

    List<UserEntity> getParticipantsByPostId(Long postId);

}
