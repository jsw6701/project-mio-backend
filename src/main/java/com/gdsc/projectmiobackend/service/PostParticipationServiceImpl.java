package com.gdsc.projectmiobackend.service;

import com.gdsc.projectmiobackend.dto.PostDto;
import com.gdsc.projectmiobackend.entity.Participants;
import com.gdsc.projectmiobackend.entity.Post;
import com.gdsc.projectmiobackend.entity.UserEntity;
import com.gdsc.projectmiobackend.repository.ParticipantsRepository;
import com.gdsc.projectmiobackend.repository.PostRepository;
import com.gdsc.projectmiobackend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostParticipationServiceImpl implements PostParticipationService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ParticipantsRepository participantsRepository;

    @Override
    public String participateInPost(Long postId, String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("유저정보가 없습니다."));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Post ID: " + postId));

        List<Participants> participants1 = participantsRepository.findByUserId(user.getId());

        for (Participants p : participants1) {
            if(p.getApproval()==true){
                return "다른 카풀에 이미 승인되었습니다.";
            }
        }

        Participants participants = new Participants(post, user);

        participantsRepository.save(participants);

        return "카풀 예약이 완료되었습니다.";
    }

    @Override
    public List<UserEntity> getParticipantsByPostId(Long postId) {
        List<Participants> participants = participantsRepository.findByPostId(postId);
        List<UserEntity> users = new ArrayList<>();
        for (Participants participant : participants) {
            users.add(participant.getUser());
        }
        return users;
    }

    @Override
    public void cancelParticipateInPost(Long postId, String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("유저정보가 없습니다."));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Post ID: " + postId));

        participantsRepository.delete(new Participants(post, user));
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

        participants.setApproval(true);

        UserEntity user = participants.getUser();

        List<Participants> participants1 = participantsRepository.findByUserId(user.getId());

        for (Participants p : participants1) {
            if (!p.getId().equals(participateId)) {
                participantsRepository.delete(p);
            }
        }

        participantsRepository.save(participants);
    }

    public PostDto getApprovalUser(String email){
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("유저정보가 없습니다."));
        List<Participants> participants = participantsRepository.findByUserId(user.getId());
        for (Participants participant : participants) {
            if(participant.getApproval()){
                return participant.getPost().toDto();
            }
        }
        return null;
    }
}
