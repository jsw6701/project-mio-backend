package com.gdsc.projectmiobackend.service;

import com.gdsc.projectmiobackend.common.ApprovalOrReject;
import com.gdsc.projectmiobackend.dto.PostDto;
import com.gdsc.projectmiobackend.entity.Alarm;
import com.gdsc.projectmiobackend.entity.Participants;
import com.gdsc.projectmiobackend.entity.Post;
import com.gdsc.projectmiobackend.entity.UserEntity;
import com.gdsc.projectmiobackend.repository.AlarmRepository;
import com.gdsc.projectmiobackend.repository.ParticipantsRepository;
import com.gdsc.projectmiobackend.repository.PostRepository;
import com.gdsc.projectmiobackend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostParticipationServiceImpl implements PostParticipationService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ParticipantsRepository participantsRepository;
    private final AlarmRepository alarmRepository;

    @Override
    public void participateInPost(Long postId, String email, String content) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("유저정보가 없습니다."));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Post ID: " + postId));

        if(participantsRepository.findByPostIdAndUserId(postId, user.getId()) != null){
            throw new IllegalArgumentException("이미 신청한 게시글입니다.");
        }
        /*
        if(Objects.equals(user.getEmail(), post.getUser().getEmail())){
            throw new IllegalArgumentException("자신의 게시글에는 신청할 수 없습니다.");
        }*/

        Participants participants = new Participants(post, user, content);

        participants.setApprovalOrReject(ApprovalOrReject.WAITING);
        participants.setVerifyFinish(false);
        participants.setDriverMannerFinish(false);
        participants.setPassengerMannerFinish(false);
        participantsRepository.save(participants);
    }

    @Override
    public Boolean checkParticipate(Long postId, String email){
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("유저정보가 없습니다."));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Post ID: " + postId));
        List<Participants> participants1 = participantsRepository.findByUserId(user.getId());

        for (Participants p : participants1) {
            if(p.getApprovalOrReject() == ApprovalOrReject.APPROVAL){
                if(p.getPost().getTargetDate().isEqual(post.getTargetDate())){
                    if(p.getPost().getVerifyGoReturn() == post.getVerifyGoReturn()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public List<Participants> getParticipantsByPostId(Long postId) {
        List<Participants> participants = participantsRepository.findByPostId(postId);

        return participants;
    }

    @Override
    public void cancelParticipateInPost(Long postId, String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("유저정보가 없습니다."));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Post ID: " + postId));

        if(Objects.equals(post.getTargetDate(), LocalDate.now())){
            throw new IllegalArgumentException("당일 카풀은 취소할 수 없습니다.");
        }

        Participants participants = participantsRepository.findByPostIdAndUserId(postId, user.getId());

        Alarm alarm = new Alarm(LocalDateTime.now(), "신청을 취소한 사람이 있습니다!", post, participants.getUser());

        alarmRepository.save(alarm);
        participantsRepository.delete(participants);
    }

    @Override
    public List<PostDto> getPostIdsByUserEmail(String email) {
        List<Participants> participants = participantsRepository.findPostListByUserId(userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("유저정보가 없습니다.")).getId());
        List<Post> posts = new ArrayList<>();
        for (Participants participant : participants) {
            posts.add(participant.getPost());
        }
        return posts.stream()
                .map(Post::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void participateApproval(Long participateId, String email) {
        Participants participants = participantsRepository.findById(participateId).orElseThrow(() -> new IllegalArgumentException("Invalid Participate ID: " + participateId));
        Post post = participants.getPost().getUser().getEmail().equals(email) ? participants.getPost() : null;

        if(post == null){
            throw new IllegalArgumentException("해당 유저는 이 게시글의 주최자가 아닙니다.");
        }

        if(post.getParticipantsCount() == null){
            post.setParticipantsCount(0L);
        }

        if(post.getNumberOfPassengers() <= post.getParticipantsCount()){
            throw new IllegalArgumentException("해당 게시글의 최대 탑승인원을 초과하였습니다.");
        }

        participants.setApprovalOrReject(ApprovalOrReject.APPROVAL);
        post.setParticipantsCount(post.getParticipantsCount() + 1);
        //UserEntity user = participants.getUser();

/*
        List<Participants> participants1 = participantsRepository.findByUserId(user.getId());

        for (Participants p : participants1) {
            if (!p.getId().equals(participateId)) {
                if(!p.getVerifyFinish())
                    participantsRepository.delete(p);
            }
        }*/


        participantsRepository.save(participants);
    }

    public void rejectParticipateInPost(Long participateId, String email){
        Participants participants = participantsRepository.findById(participateId).orElseThrow(() -> new IllegalArgumentException("Invalid Participate ID: " + participateId));
        Post post = participants.getPost().getUser().getEmail().equals(email) ? participants.getPost() : null;

        if(post == null){
            throw new IllegalArgumentException("해당 유저는 이 게시글의 주최자가 아닙니다.");
        }

        if(participants.getApprovalOrReject() == ApprovalOrReject.APPROVAL){
            throw new IllegalArgumentException("이미 승인된 유저입니다.");
        }

        if(post.getParticipantsCount() == null){
            post.setParticipantsCount(0L);
        }

        participants.setApprovalOrReject(ApprovalOrReject.REJECT);

        participantsRepository.delete(participants);
    }


    @Override
    public PostDto getApprovalUser(String email){
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("유저정보가 없습니다."));
        List<Participants> participants = participantsRepository.findByUserId(user.getId());
        for (Participants participant : participants) {
            if(participant.getApprovalOrReject() == ApprovalOrReject.APPROVAL){
                return participant.getPost().toDto();
            }
        }
        return null;
    }
}
