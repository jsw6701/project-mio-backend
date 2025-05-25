package com.gdsc.projectmiobackend.service;

import com.gdsc.projectmiobackend.dto.ParticipateCheckDto;
import com.gdsc.projectmiobackend.dto.ParticipateDto;
import com.gdsc.projectmiobackend.dto.ParticipateMsgDto;
import com.gdsc.projectmiobackend.dto.PostDto;
import com.gdsc.projectmiobackend.dto.request.ParticipateCreateRequestDto;
import com.gdsc.projectmiobackend.entity.Participants;

import java.util.List;

public interface PostParticipationService {
    ParticipateDto participateInPost(Long postId, String email, String content);

    ParticipateDto participateInPostV2(Long postId, String email, ParticipateCreateRequestDto participateCreateRequestDto);

    ParticipateCheckDto checkParticipate(Long postId, String email);

    List<Participants> getParticipantsByPostId(Long postId);

    ParticipateMsgDto cancelParticipateInPost(Long postId, String email);

    List<ParticipateDto> getPostIdsByUserEmail(String email);

    ParticipateMsgDto participateApproval(Long participantId, String email);

    List<PostDto> getApprovalUser(String email);

    ParticipateMsgDto rejectParticipateInPost(Long participateId, String email);
}
