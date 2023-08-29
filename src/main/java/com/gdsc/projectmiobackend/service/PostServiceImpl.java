package com.gdsc.projectmiobackend.service;


import com.gdsc.projectmiobackend.common.ApprovalOrReject;
import com.gdsc.projectmiobackend.dto.ParticipateGetDto;
import com.gdsc.projectmiobackend.dto.PostDto;
import com.gdsc.projectmiobackend.dto.request.*;
import com.gdsc.projectmiobackend.entity.*;
import com.gdsc.projectmiobackend.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService{

    private final PostRepository postRepository;

    private final CategoryRepository categoryRepository;

    private final UserRepository userRepository;

    private final ParticipantsRepository participantsRepository;

    private final MannerEntityRepository mannerEntityRepository;

    @Override
    public Post findById(Long id) {
        return postRepository.findById(id).orElse(new Post());
    }


    @Override
    public Post addPostList(PostCreateRequestDto postCreateRequestDto, Long categoryId, String email){
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("유저정보가 없습니다."));

        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new IllegalArgumentException("TODO 생성실패"));
        return postRepository.save(postCreateRequestDto.toEntity(category, user));
    }

    @Override
    public Post updateById(Long id, PostPatchRequestDto postPatchRequestDto, String email){
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("유저정보가 없습니다."));
        Post post = this.findById(id);
        Category category = categoryRepository.findById(postPatchRequestDto.getCategoryId()).orElseThrow(() -> new IllegalArgumentException("TODO 생성실패"));

        if (!Objects.equals(post.getUser().getEmail(), user.getEmail())) {
            throw new IllegalStateException("해당 글을 수정할 권한이 없습니다.");
        }

        post.setCategory(category);
        post.setContent(postPatchRequestDto.getContent());

        return this.postRepository.save(post);
    }

    @Override
    public Post updateFinishById(Long id, PostVerifyFinishRequestDto postPatchRequestDto, String email){
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("유저정보가 없습니다."));
        Post post = this.findById(id);

        if (!Objects.equals(post.getUser().getEmail(), user.getEmail())) {
            throw new IllegalStateException("해당 글을 수정할 권한이 없습니다.");
        }

        post.setVerifyFinish(postPatchRequestDto.getVerifyFinish());

        List<Participants> participantsList = post.getParticipants();

        for (Participants participants : participantsList) {
            if(participants.getApprovalOrReject() == ApprovalOrReject.APPROVAL){
                participants.setApprovalOrReject(ApprovalOrReject.FINISH);
                participants.setVerifyFinish(true);
            }
            else{
                this.participantsRepository.delete(participants);
            }
        }

        return this.postRepository.save(post);
    }

    @Override
    public void deletePostList(Long id, String email) {
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("유저정보가 없습니다."));
        Post post = this.findById(id);
        if (!Objects.equals(post.getUser().getEmail(), user.getEmail())) {
            throw new IllegalStateException("해당 글을 삭제할 권한이 없습니다.");
        }
        postRepository.deleteById(id);
    }

    @Override
    public Page<PostDto> findPostList(Pageable pageable) {
        Page<Post> page = postRepository.findAll(pageable);
        return page.map(Post::toDto);
    }

    @Override
    public Page<PostDto> findByCategoryId(Long categoryId, Pageable pageable){
        Category category = this.categoryRepository.findById(categoryId).orElseThrow(() -> new IllegalArgumentException("카테고리가 없습니다."));
        Page<Post> page = postRepository.findByCategory(category, pageable);
        return page.map(Post::toDto);
    }

    @Override
    public Page<PostDto> findByMemberId(Long userId, Pageable pageable){
        UserEntity user = this.userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("유저가 없습니다."));
        Page<Post> page = postRepository.findByUser(user, pageable);
        return page.map(Post::toDto);
    }

    @Override
    public Post showDetailPost(Long id){

        Post post = this.findById(id);

        post.setViewCount(post.getViewCount() + 1);

        this.postRepository.save(post);
        return postRepository.findById(id).orElse(null);
    }

    @Override
    public List<PostDto> findByLatitudeAndLongitude(Double latitude, Double longitude){
        List<Post> postList = postRepository.findByLatitudeAndLongitude(latitude, longitude);
        return postList.stream().map(Post::toDto).toList();
    }

    @Override
    public ParticipateGetDto getApprovalUserCountByPost(Long postId){
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Invalid Post ID: " + postId));
        return new ParticipateGetDto(post.getParticipantsCount(), post.getNumberOfPassengers());
    }

    @Override
    public void driverUpdateManner(Long id, String email, MannerDriverUpdateRequestDto mannerDriverUpdateRequestDto){
        UserEntity currentUser = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("유저정보가 없습니다."));
        Post post = this.findById(id);


        if(currentUser.getMannerCount() == null){
            currentUser.setMannerCount(0L);
        }

        if(!post.getVerifyFinish()){
            throw new IllegalStateException("해당 글은 마감되지 않았습니다.");
        }

        List<Participants> participants = post.getParticipants();

        UserEntity driver = post.getUser();

        if(driver.getMannerCount() == null){
            driver.setMannerCount(0L);
        }

        Long driverMannerCount = driver.getMannerCount();

        if (!Objects.equals(post.getUser().getEmail(), currentUser.getEmail())) {
            if(participants.stream().anyMatch(participant -> Objects.equals(participant.getUser().getEmail(), currentUser.getEmail()))){

                Participants participants1 = participants.stream().filter(participant -> Objects.equals(participant.getUser().getEmail(), currentUser.getEmail())).findFirst().orElseThrow(() -> new IllegalArgumentException("참여자가 아닙니다."));

                if(participants1.getDriverMannerFinish()){
                    throw new IllegalStateException("이미 평가한 운전자입니다.");
                }

                participants1.setDriverMannerFinish(true);
                participantsRepository.save(participants1);

                switch (mannerDriverUpdateRequestDto.getManner()) {
                    case "good" -> driver.setMannerCount(driverMannerCount + 1);
                    case "bad" -> driver.setMannerCount(driverMannerCount - 1);
                    case "normal" -> driver.setMannerCount(driverMannerCount);
                    default -> throw new IllegalStateException("잘못된 평가입니다.");
                }


            }
            else{
                throw new IllegalStateException("해당 글에 참여하지 않았습니다.");
            }
        }

        else{
            throw new IllegalStateException("해당 글의 운전자입니다.");
        }
        Long updateMannerCount = driver.getMannerCount();

        driver.setGrade(calculateGrade(updateMannerCount));



        MannerEntity mannerEntity = new MannerEntity(mannerDriverUpdateRequestDto.getManner(), mannerDriverUpdateRequestDto.getContent(), driver.getId(), LocalDateTime.now());

        mannerEntityRepository.save(mannerEntity);
        this.userRepository.save(driver);
    }

    @Override
    public void updateParticipatesManner(Long userId, MannerPassengerUpdateRequestDto mannerPassengerUpdateRequestDto, String email){
        UserEntity targetUser = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("유저정보가 없습니다."));
        UserEntity currentUser = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("유저정보가 없습니다."));

        Participants participants = participantsRepository.findByPostIdAndUserId(mannerPassengerUpdateRequestDto.getPostId(), userId);

        if (Objects.equals(targetUser.getEmail(), currentUser.getEmail())) {
            throw new IllegalStateException("자기 자신을 평가할 수 없습니다.");
        }

        if(participants.getPassengerMannerFinish()){
            throw new IllegalStateException("이미 평가한 유저입니다.");
        }

        if(currentUser.getMannerCount() == null){
            currentUser.setMannerCount(0L);
        }
        if(targetUser.getMannerCount() == null){
            targetUser.setMannerCount(0L);
        }

        Long targetUserMannerCount = targetUser.getMannerCount();

        switch (mannerPassengerUpdateRequestDto.getManner()) {
            case "good" -> targetUser.setMannerCount(targetUserMannerCount + 1);
            case "bad" -> targetUser.setMannerCount(targetUserMannerCount - 1);
            case "normal" -> targetUser.setMannerCount(targetUserMannerCount);
            default -> throw new IllegalStateException("잘못된 평가입니다.");
        }

        participants.setPassengerMannerFinish(true);
        participantsRepository.save(participants);
        targetUser.setGrade(calculateGrade(targetUser.getMannerCount()));

        MannerEntity mannerEntity = new MannerEntity(mannerPassengerUpdateRequestDto.getManner(), mannerPassengerUpdateRequestDto.getContent(), targetUser.getId(), LocalDateTime.now());
        mannerEntityRepository.save(mannerEntity);
        this.userRepository.save(targetUser);
    }

    private String calculateGrade(Long mannerCount) {
        if (mannerCount <= -1) {
            return "F";
        } else if (mannerCount <= 9) {
            return "D";
        } else if (mannerCount <= 19) {
            return "D+";
        } else if (mannerCount <= 29) {
            return "C";
        } else if (mannerCount <= 39) {
            return "C+";
        } else if (mannerCount <= 49) {
            return "B";
        } else if (mannerCount <= 59) {
            return "B+";
        } else if (mannerCount <= 69) {
            return "A";
        } else if (mannerCount <= 79) {
            return "A+";
        } else if (mannerCount <= 89) {
            return "MIO 조교님";
        } else {
            return "MIO 교수님";
        }
    }

    @Override
    public Page<PostDto> findByParticipate(String email, Pageable pageable){
        UserEntity user = this.userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("유저가 없습니다."));
        List<Participants> participants = this.participantsRepository.findByUserId(user.getId());
        Page<PostDto> page = new PageImpl<>(participants.stream().map(Participants::getPost).map(Post::toDto).toList(), pageable, participants.size());
        return page;
    }

    @Override
    public List<PostDto> findByLocation(String location){
        List<Post> postList = postRepository.findByLocation(location);
        return postList.stream().map(Post::toDto).toList();
    }
}
