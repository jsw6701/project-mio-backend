package com.gdsc.projectmiobackend.service;


import com.gdsc.projectmiobackend.dto.ParticipateGetDto;
import com.gdsc.projectmiobackend.dto.PostDto;
import com.gdsc.projectmiobackend.dto.PostMsgDto;
import com.gdsc.projectmiobackend.dto.request.*;
import com.gdsc.projectmiobackend.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

public interface PostService {

    Post findById(Long id);

    PostDto addPost(PostCreateRequestDto postCreateRequestDto, Long categoryId, String email) throws IOException;

    PostMsgDto deletePostList(Long id, String email);

    PostDto updateById(Long id, PostPatchRequestDto postPatchRequestDto, String email);

    PostDto updateTypeChangeById(Long postId,  String email);

    PostDto updateFinishById(Long id, String email);

    Page<PostDto> findPostList(Pageable pageable);

    Page<PostDto> findByRegion3Depth(String email, Pageable pageable);

    @Transactional(readOnly = true)
    Page<PostDto> findMainPagePaging(Pageable pageable, PostMainPageRequestDto postMainPageRequestDto);

    @Transactional(readOnly = true)
    List<PostDto> findMainPageList(PostMainPageRequestDto postMainPageRequestDto);

    Page<PostDto> findByCategoryId(Long categoryId, Pageable pageable);

    Page<PostDto> findByMemberId(Long userId, Pageable pageable);

    PostDto showDetailPost(Long id);

    List<PostDto> findByLatitudeAndLongitude(Double latitude, Double longitude);

    ParticipateGetDto getApprovalUserCountByPost(Long postId);

    PostMsgDto driverUpdateManner(Long id, String email, MannerDriverUpdateRequestDto mannerDriverUpdateRequestDto);

    PostMsgDto updateParticipatesManner(Long userId, MannerPassengerUpdateRequestDto mannerPassengerUpdateRequestDto, String email);

    Page<PostDto> findByParticipate(String email, Pageable pageable);

    List<PostDto> findByLocation(String location);

    Page<PostDto> reviewsCanBeWritten(String email, Pageable pageable);

    List<PostDto> findByDistance(Long postId);
}
