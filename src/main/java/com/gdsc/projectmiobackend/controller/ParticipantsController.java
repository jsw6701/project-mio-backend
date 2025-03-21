package com.gdsc.projectmiobackend.controller;

import com.gdsc.projectmiobackend.dto.ParticipateCheckDto;
import com.gdsc.projectmiobackend.dto.ParticipateDto;
import com.gdsc.projectmiobackend.dto.ParticipateMsgDto;
import com.gdsc.projectmiobackend.dto.PostDto;
import com.gdsc.projectmiobackend.dto.request.ParticipateCreateRequestDto;
import com.gdsc.projectmiobackend.entity.Participants;
import com.gdsc.projectmiobackend.jwt.dto.UserInfo;
import com.gdsc.projectmiobackend.service.PostParticipationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@Tag(name = "게시글 참여 유저")
public class ParticipantsController {

    private final PostParticipationService participantsService;
    @Operation(summary = "유저 게시글 참여")
    @PostMapping("/{postId}/participate")
    public ResponseEntity<ParticipateDto> participateInPost(@PathVariable Long postId,
                                                            @RequestBody ParticipateCreateRequestDto participateCreateRequestDto,
                                                            @AuthenticationPrincipal UserInfo user) {
        ParticipateDto participateDto = participantsService.participateInPost(postId, user.getEmail(), participateCreateRequestDto.getContent());
        return ResponseEntity.ok(participateDto);
    }

    @Operation(summary = "같은 날 신청하려는 게시글과 같은 등/하교 승인된 게시글이 있는지 확인(있다면 false 없으면 true)")
    @GetMapping("/{postId}/check")
    public ResponseEntity<ParticipateCheckDto> checkParticipate(@PathVariable Long postId, @AuthenticationPrincipal UserInfo user) {
        ParticipateCheckDto participateCheckDto = participantsService.checkParticipate(postId, user.getEmail());
        return ResponseEntity.ok(participateCheckDto);
    }

    @Operation(summary = "게시글 별 참여자 조회")
    @GetMapping("/{postId}/participants")
    public ResponseEntity<List<ParticipateDto>> getParticipantsByPostId(@PathVariable Long postId) {

        List<Participants> participants = participantsService.getParticipantsByPostId(postId);
        return ResponseEntity.ok(participants.stream().map(Participants::toDto).toList());
    }

    @Operation(summary = "유저 게시글 참여 취소")
    @PatchMapping("/{postId}/participateCancel")
    public ResponseEntity<ParticipateMsgDto> cancelParticipateInPost(@PathVariable Long postId, @AuthenticationPrincipal UserInfo user) {
        ParticipateMsgDto participateMsgDto = participantsService.cancelParticipateInPost(postId, user.getEmail());
        return ResponseEntity.ok(participateMsgDto);
    }

    @Operation(summary = "참여 거절하기")
    @PatchMapping("/{participantId}/reject")
    public ResponseEntity<ParticipateMsgDto> rejectParticipateInPost(@PathVariable Long participantId, @AuthenticationPrincipal UserInfo user) {
        ParticipateMsgDto participateMsgDto = participantsService.rejectParticipateInPost(participantId, user.getEmail());
        return ResponseEntity.ok(participateMsgDto);
    }

    @Operation(summary = "마이페이지 > 유저가 참여한 게시글 조회 (게시글 작성자 미포함)")
    @GetMapping("/user/participants")
    public ResponseEntity<List<ParticipateDto>> getParticipantsByUserId(@AuthenticationPrincipal UserInfo user) {

        List<ParticipateDto> participateDtos = participantsService.getPostIdsByUserEmail(user.getEmail());
        return ResponseEntity.ok(participateDtos);
    }

    @Operation(summary = "게시글 참여 승인")
    @PatchMapping("/{participantId}/participateAccept")
    public ResponseEntity<ParticipateMsgDto> participateInParticipant(@PathVariable Long participantId, @AuthenticationPrincipal UserInfo user) {
        ParticipateMsgDto participateMsgDto = participantsService.participateApproval(participantId, user.getEmail());
        return ResponseEntity.ok(participateMsgDto);
    }

    @Operation(summary = "메인페이지 > 상단 > 유저별 카풀 승인 현황 (게시글 작성자 포함)")
    @GetMapping("/user/participants/carpool")
    public ResponseEntity<List<PostDto>> getParticipantsByUserIdAndCarpool(@AuthenticationPrincipal UserInfo user) {

        List<PostDto> post = participantsService.getApprovalUser(user.getEmail());
        return ResponseEntity.ok(post);
    }
}
