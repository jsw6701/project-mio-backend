package com.gdsc.projectmiobackend.controller;

import com.gdsc.projectmiobackend.dto.ParticipateDto;
import com.gdsc.projectmiobackend.dto.PostDto;
import com.gdsc.projectmiobackend.dto.request.ParticipateCreateRequestDto;
import com.gdsc.projectmiobackend.entity.Participants;
import com.gdsc.projectmiobackend.jwt.dto.UserInfo;
import com.gdsc.projectmiobackend.service.PostParticipationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@Tag(name = "게시글 참여 유저")
public class ParticipantsController {

    private final PostParticipationService participantsService;
    @Operation(summary = "유저 게시글 참여")
    @PostMapping("/{postId}/participate")
    public ResponseEntity<String> participateInPost(@PathVariable Long postId,
                                                    @RequestBody ParticipateCreateRequestDto participateCreateRequestDto,
                                                    @AuthenticationPrincipal UserInfo user) {

        return ResponseEntity.ok(participantsService.participateInPost(postId, user.getEmail(), participateCreateRequestDto.getContent()));
    }

    @Operation(summary = "게시글 별 참여자 조회")
    @GetMapping("/{postId}/participants")
    public ResponseEntity<List<ParticipateDto>> getParticipantsByPostId(@PathVariable Long postId) {

        List<Participants> participants = participantsService.getParticipantsByPostId(postId);

        return ResponseEntity.ok(participants.stream().map(ParticipateDto::new).collect(Collectors.toList()));
    }

    @Operation(summary = "유저 게시글 참여 취소")
    @DeleteMapping("/{postId}/participate")
    public ResponseEntity<Void> cancelParticipateInPost(@PathVariable Long postId, @AuthenticationPrincipal UserInfo user) {
        participantsService.cancelParticipateInPost(postId, user.getEmail());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "참여 거절하기")
    @DeleteMapping("/{participantId}/reject")
    public ResponseEntity<Void> rejectParticipateInPost(@PathVariable Long participantId, @AuthenticationPrincipal UserInfo user) {
        participantsService.rejectParticipateInPost(participantId, user.getEmail());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "유저가 참여한 게시글 조회")
    @PageableAsQueryParam
    @GetMapping("/user/participants")
    public ResponseEntity<List<PostDto>> getParticipantsByUserId(@AuthenticationPrincipal UserInfo user) {

        List<PostDto> posts = participantsService.getPostIdsByUserEmail(user.getEmail());
        return ResponseEntity.ok(posts);
    }

    @Operation(summary = "게시글 참여 승인")
    @PatchMapping("/{participantId}/participate")
    public ResponseEntity<Void> participateInParticipant(@PathVariable Long participantId, @AuthenticationPrincipal UserInfo user) {
        participantsService.participateApproval(participantId, user.getEmail());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "유저별 카풀 승인 현황")
    @GetMapping("/user/participants/carpool")
    public ResponseEntity<PostDto> getParticipantsByUserIdAndCarpool(@AuthenticationPrincipal UserInfo user) {

        PostDto post = participantsService.getApprovalUser(user.getEmail());
        return ResponseEntity.ok(post);
    }
}
