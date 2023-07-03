package com.gdsc.projectmiobackend.service;

import com.gdsc.projectmiobackend.dto.PostDto;
import com.gdsc.projectmiobackend.entity.UserEntity;
import java.util.List;

public interface PostParticipationService {
    String participateInPost(Long postId, String email);

    List<UserEntity> getParticipantsByPostId(Long postId);

    void cancelParticipateInPost(Long postId, String email);

    List<PostDto> getPostIdsByUserEmail(String email);

    void participateApproval(Long participantId, String email);

    PostDto getApprovalUser(String email);

    void rejectParticipateInPost(Long participateId, String email);
}
