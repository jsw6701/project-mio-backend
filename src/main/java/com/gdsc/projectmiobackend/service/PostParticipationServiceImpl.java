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
    public void participateInPost(Long postId, String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("유저정보가 없습니다."));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Post ID: " + postId));

        Participants participants = new Participants(post, user);

        participantsRepository.save(participants);
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
}
