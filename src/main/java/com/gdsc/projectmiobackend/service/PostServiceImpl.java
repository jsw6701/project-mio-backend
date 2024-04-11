package com.gdsc.projectmiobackend.service;


import com.gdsc.projectmiobackend.common.ApprovalOrReject;
import com.gdsc.projectmiobackend.dto.ParticipateGetDto;
import com.gdsc.projectmiobackend.dto.PostDto;
import com.gdsc.projectmiobackend.dto.request.*;
import com.gdsc.projectmiobackend.entity.*;
import com.gdsc.projectmiobackend.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    private UserEntity getUserByEmail(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("유저 정보를 찾을 수 없습니다. 이메일: " + email));
    }

    private Post getPostById(Long id){
        return postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다. ID: " + id));
    }

    private void checkPostUser(Post post, UserEntity user){
        if (!Objects.equals(post.getUser().getEmail(), user.getEmail())) {
            throw new IllegalStateException("게시물을 수정할 권한이 없습니다. 게시물 ID: " + post.getId());
        }
    }

    @Override
    public Post findById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다. ID: "+ id));
    }


    @Override
    @CacheEvict(value = "postCache", allEntries=true)
    public Post addPostList(PostCreateRequestDto postCreateRequestDto, Long categoryId, String email){
        UserEntity user = getUserByEmail(email);

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("TODO 생성실패"));

        Post post = Post.builder()
                .title(postCreateRequestDto.getTitle())
                .content(postCreateRequestDto.getContent())
                .targetDate(postCreateRequestDto.getTargetDate())
                .targetTime(postCreateRequestDto.getTargetTime())
                .category(category)
                .verifyGoReturn(postCreateRequestDto.getVerifyGoReturn())
                .numberOfPassengers(postCreateRequestDto.getNumberOfPassengers())
                .latitude(postCreateRequestDto.getLatitude())
                .longitude(postCreateRequestDto.getLongitude())
                .location(postCreateRequestDto.getLocation())
                .cost(postCreateRequestDto.getCost())
                .user(user)
                .createDate(LocalDateTime.now())
                .verifyFinish(postCreateRequestDto.getVerifyFinish())
                .build();

        Participants participants = Participants.builder()
                .post(post)
                .user(user)
                .content("작성자")
                .approvalOrReject(ApprovalOrReject.APPROVAL)
                .verifyFinish(false)
                .driverMannerFinish(false)
                .passengerMannerFinish(false)
                .postUserId(user.getId())
                .build();

        postRepository.save(post);
        participantsRepository.save(participants);

        return post;
    }

    @Override
    @CacheEvict(value = "postCache", allEntries=true)
    public Post updateById(Long id, PostPatchRequestDto postPatchRequestDto, String email){
        UserEntity user = getUserByEmail(email);
        Post post = getPostById(id);

        Category category = categoryRepository.findById(postPatchRequestDto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다. " + postPatchRequestDto.getCategoryId()));

        checkPostUser(post, user);

        Post updatePost = post.toBuilder()
                .title(postPatchRequestDto.getTitle())
                .content(postPatchRequestDto.getContent())
                .targetDate(postPatchRequestDto.getTargetDate())
                .targetTime(postPatchRequestDto.getTargetTime())
                .category(category)
                .numberOfPassengers(postPatchRequestDto.getNumberOfPassengers())
                .latitude(postPatchRequestDto.getLatitude())
                .longitude(postPatchRequestDto.getLongitude())
                .location(postPatchRequestDto.getLocation())
                .cost(postPatchRequestDto.getCost())
                .build();

        return this.postRepository.save(updatePost);
    }

    @Override
    @CacheEvict(value = "postCache", allEntries=true)
    public Post updateFinishById(Long id, PostVerifyFinishRequestDto postPatchRequestDto, String email){
        UserEntity user = getUserByEmail(email);
        Post post = getPostById(id);

        checkPostUser(post, user);

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
    @CacheEvict(value = "postCache", allEntries=true)
    public void deletePostList(Long id, String email) {
        UserEntity user = getUserByEmail(email);
        Post post = getPostById(id);

        checkPostUser(post, user);

        postRepository.deleteById(id);
    }

    @Override
    @Cacheable(value="postCache", key="#pageable")
    public Page<PostDto> findPostList(Pageable pageable) {
        Page<Post> page = postRepository.findAll(pageable);
        return page.map(Post::toDto);
    }

    @Override
    public Page<PostDto> findByCategoryId(Long categoryId, Pageable pageable){
        Category category = this.categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다. " + categoryId));
        Page<Post> page = postRepository.findByCategory(category, pageable);
        return page.map(Post::toDto);
    }

    @Override
    public Page<PostDto> findByMemberId(Long userId, Pageable pageable){
        UserEntity user = this.userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저정보가 없습니다. 아이디: " + userId));
        Page<Post> page = postRepository.findByUser(user, pageable);
        return page.map(Post::toDto);
    }

    @Override
    public Post showDetailPost(Long id){

        Post post = getPostById(id);

        post.setViewCount(post.getViewCount() + 1);

        postRepository.save(post);
        return postRepository.findById(id).orElse(null);
    }

    @Override
    public List<PostDto> findByLatitudeAndLongitude(Double latitude, Double longitude){
        List<Post> postList = postRepository.findByLatitudeAndLongitude(latitude, longitude);
        return postList.stream().map(Post::toDto).toList();
    }

    @Override
    public ParticipateGetDto getApprovalUserCountByPost(Long postId){
        Post post = getPostById(postId);
        return new ParticipateGetDto(post.getParticipantsCount(), post.getNumberOfPassengers());
    }

    @Override
    public void driverUpdateManner(Long postId, String email, MannerDriverUpdateRequestDto mannerDriverUpdateRequestDto){
        UserEntity currentUser = getUserByEmail(email);

        Post post = getPostById(postId);

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
                    case GOOD -> driver.setMannerCount(driverMannerCount + 1);
                    case BAD -> driver.setMannerCount(driverMannerCount - 1);
                    case NORMAL -> driver.setMannerCount(driverMannerCount);
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



        MannerEntity mannerEntity = new MannerEntity(mannerDriverUpdateRequestDto.getManner(), mannerDriverUpdateRequestDto.getContent(), driver.getId(), currentUser.getId(), LocalDateTime.now());
        mannerEntityRepository.save(mannerEntity);
        this.userRepository.save(driver);
    }

    @Override
    public void updateParticipatesManner(Long userId, MannerPassengerUpdateRequestDto mannerPassengerUpdateRequestDto, String email){
        UserEntity targetUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("운전자의 유저정보가 없습니다."));
        UserEntity currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("로그인 유저정보가 없습니다."));

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
            case GOOD -> targetUser.setMannerCount(targetUserMannerCount + 1);
            case BAD -> targetUser.setMannerCount(targetUserMannerCount - 1);
            case NORMAL -> targetUser.setMannerCount(targetUserMannerCount);
            default -> throw new IllegalStateException("잘못된 평가입니다.");
        }

        participants.setPassengerMannerFinish(true);
        participantsRepository.save(participants);
        targetUser.setGrade(calculateGrade(targetUser.getMannerCount()));

        MannerEntity mannerEntity = new MannerEntity(mannerPassengerUpdateRequestDto.getManner(), mannerPassengerUpdateRequestDto.getContent(), targetUser.getId(), currentUser.getId(), LocalDateTime.now());
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
        return new PageImpl<>(participants.stream().map(Participants::getPost).map(Post::toDto).toList(), pageable, participants.size());
    }

    @Override
    public List<PostDto> findByLocation(String location) {

        if(location == null || location.isEmpty() || location.isBlank()){
            throw new IllegalArgumentException("지역을 입력해주세요.");
        }

        List<Post> postList = postRepository.findByLocationContaining(location);
        return postList.stream().map(Post::toDto).toList();
    }

    @Override
    public Page<PostDto> reviewsCanBeWritten(String email, Pageable pageable) {
        UserEntity user = getUserByEmail(email);

        // 탑승자로 참여한 리스트
        List<Participants> participants = this.participantsRepository.findByUserId(user.getId());

        // 운전자로 참여한 리스트
        List<Participants> participants1 = this.participantsRepository.findByPostUserId(user.getId());

        List<Post> posts = new ArrayList<>();

        // 탑승자로 참여한 리스트 중 운전자 평가를 하지 않은 리스트
        for(Participants p : participants){
            if(p.getApprovalOrReject() == ApprovalOrReject.FINISH && p.getDriverMannerFinish() == false){
                posts.add(p.getPost());
            }
        }


        // 운전자로 참여한 리스트 중 탑승자 평가를 하지 않은 리스트
        for(Participants p : participants1){
            if(p.getApprovalOrReject() == ApprovalOrReject.FINISH && p.getPassengerMannerFinish() == false){
                posts.add(p.getPost());
            }
        }

        return new PageImpl<>(posts.stream().map(Post::toDto).toList(), pageable, posts.size());
    }


    @Override
    public List<PostDto> findByDistance(Long postId) {
        /*List<Post> postList = postRepository.findAll();
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Invalid Post ID: " + postId));
        List<Post> postList1 = new ArrayList<>();

        double lat = post.getLatitude();
        double lon = post.getLongitude();

        for(Post p : postList){
            double lat1 = p.getLatitude();
            double lon1 = p.getLongitude();
            double theta = lon - lon1;
            double dist = Math.sin(deg2rad(lat)) * Math.sin(deg2rad(lat1)) + Math.cos(deg2rad(lat)) * Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(theta));
            dist = Math.acos(dist);
            dist = rad2deg(dist);
            dist = dist * 60 * 1.1515 * 1.609344;
            if(dist <= 3){
                postList1.add(p);
            }
        }*/

        List<Post> postList = postRepository.findByDistance(postId);

        return postList.stream().map(Post::toDto).toList();
    }

/*    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }*/
}
