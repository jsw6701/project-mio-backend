package com.gdsc.projectmiobackend.service;


import com.gdsc.projectmiobackend.common.ApprovalOrReject;
import com.gdsc.projectmiobackend.common.PostType;
import com.gdsc.projectmiobackend.dto.ParticipateGetDto;
import com.gdsc.projectmiobackend.dto.PostDto;
import com.gdsc.projectmiobackend.dto.PostMsgDto;
import com.gdsc.projectmiobackend.dto.request.MannerDriverUpdateRequestDto;
import com.gdsc.projectmiobackend.dto.request.MannerPassengerUpdateRequestDto;
import com.gdsc.projectmiobackend.dto.request.PostCreateRequestDto;
import com.gdsc.projectmiobackend.dto.request.PostPatchRequestDto;
import com.gdsc.projectmiobackend.entity.*;
import com.gdsc.projectmiobackend.notification.service.impl.NotificationServiceImpl;
import com.gdsc.projectmiobackend.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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

    private final NotificationServiceImpl notificationService;

    private final AlarmRepository alarmRepository;

    /**
     * 이메일로 유저 정보를 가져옵니다.
     * @param email 유저 이메일
     * @return UserEntity 유저 엔티티
     */
    private UserEntity getUserByEmail(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("유저 정보를 찾을 수 없습니다. 이메일: " + email));
    }

    /**
     * 게시물 ID로 게시물 정보를 가져옵니다.
     * @param id 게시물 ID
     * @return Post 게시물 엔티티
     */
    private Post getPostById(Long id){
        return postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다. ID: " + id));
    }

    /**
     * 게시물 작성자인지 확인합니다.
     * @param post 게시물 엔티티
     * @param user 유저 엔티티
     */
    private void checkPostUser(Post post, UserEntity user){
        if (!Objects.equals(post.getUser().getEmail(), user.getEmail())) {
            throw new IllegalStateException("게시물을 수정할 권한이 없습니다. 게시물 ID: " + post.getId());
        }
    }

    @Override
    public Post findById(Long id) {
        return getPostById(id);
    }

    /**
     * 게시글 생성 및 작성자 참여
     * @param postCreateRequestDto
     * @param categoryId
     * @param email
     * @return
     */

    @Override
    @Transactional
    //@CacheEvict(value = "postCache", allEntries=true)
    public PostDto addPost(PostCreateRequestDto postCreateRequestDto, Long categoryId, String email){
        UserEntity user = getUserByEmail(email);
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("TODO 생성실패"));
        Post post = postCreateRequestDto.toEntity(user, category);
        Participants participants = Participants.builder()
                .post(post)
                .user(user)
                .content("작성자")
                .approvalOrReject(ApprovalOrReject.APPROVAL)
                .verifyFinish(false)
                .driverMannerFinish(false)
                .passengerMannerFinish(false)
                .postUserId(user.getId())
                .isDeleteYN("N")
                .build();

        postRepository.save(post);
        participantsRepository.save(participants);

        return post.toDto();
    }

    /**
     * 게시물 수정
     * @param id
     * @param postPatchRequestDto
     * @param email
     * @return PostDto
     */
    @Override
    @Transactional
    //@CacheEvict(value = "postCache", key = "#id")
    public PostDto updateById(Long id, PostPatchRequestDto postPatchRequestDto, String email){
        UserEntity user = getUserByEmail(email);
        Post post = getPostById(id);
        Category category = categoryRepository.findById(postPatchRequestDto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다. " + postPatchRequestDto.getCategoryId()));

        checkPostUser(post, user);

        Post updatePost = postPatchRequestDto.toEntity(post, category);
        postRepository.save(updatePost);

        return updatePost.toDto();
    }

    /**
     * 게시글 마감(신청 끝)
     * @param postId
     * @param email
     * @return PostDto
     */
    @Override
    @Transactional
    //@CacheEvict(value = "postCache", key = "#postId")
    public PostDto updateTypeChangeById(Long postId, String email) {
        UserEntity user = getUserByEmail(email);
        Post post = getPostById(postId);
        checkPostUser(post, user);

        post.setPostType(PostType.DEADLINE);

        postRepository.save(post);

        return post.toDto();
    }

    /**
     * 게시물 완료(도착)
     * @param id
     * @param email
     * @return PostDto
     */
    @Override
    @Transactional
    //@CacheEvict(value = "postCache", key = "#id")
    public PostDto updateFinishById(Long id, String email){
        UserEntity user = getUserByEmail(email);
        Post post = getPostById(id);

        checkPostUser(post, user);

        post.setPostType(PostType.COMPLETED);

        List<Participants> participantsList = post.getParticipants();

        for (Participants participants : participantsList) {
            if(participants.getApprovalOrReject() == ApprovalOrReject.APPROVAL){
                participants.setApprovalOrReject(ApprovalOrReject.FINISH);
                participants.setVerifyFinish(true);
                // 카풀이 완료되어 탑승자들에게 후기 작성하라는 알림 발송
                if(participants.getUser().getId() != post.getUser().getId()) {
                    notificationService.customNotify(
                            participants.getUser().getId(),
                            post.getId()+":"+post.getUser().getStudentId() + " 님과의 카풀은 어떠셨나요? 후기를 작성해주세요.",
                            post.getUser().getStudentId() + " 님과의 카풀은 어떠셨나요? 후기를 작성해주세요.",
                            "participate");
                    Alarm alarm = Alarm.builder()
                            .post(post)
                            .userEntity(participants.getUser())
                            .content(post.getUser().getStudentId() + " 님과의 카풀은 어떠셨나요? 후기를 작성해주세요.")
                            .createDate(LocalDateTime.now())
                            .build();
                    alarmRepository.save(alarm);
                }
            }
            else{
                this.participantsRepository.delete(participants);
            }
        }

        // 카풀 종료시 운전자에게 후기 작성하라는 알림 발송
        notificationService.customNotify(
                post.getUser().getId(),
                post.getId()+":"+"오늘 카풀은 어떠셨나요? 탑승자분들의 후기를 작성해주세요.",
                "오늘 카풀은 어떠셨나요? 탑승자분들의 후기를 작성해주세요.", "participate");
        Alarm alarm = Alarm.builder()
                .post(post)
                .userEntity(post.getUser())
                .content("오늘 카풀은 어떠셨나요? 탑승자분들의 후기를 작성해주세요.")
                .createDate(LocalDateTime.now())
                .build();
        alarmRepository.save(alarm);
        postRepository.save(post);

        return post.toDto();
    }

    /**
     * 게시글 삭제 isDeleteYN
     * @param id
     * @param email
     * @return PostMsgDto
     */
    @Override
    @Transactional
    //@CacheEvict(value = "postCache", key = "#id")
    public PostMsgDto deletePostList(Long id, String email) {
        Post post = getPostById(id);

        if(!post.getPostType().equals(PostType.BEFORE_DEADLINE)) {
            throw new IllegalStateException("마감 후에는 게시글을 지울 수 없습니다.");
        }

        if(post.getTargetDate().equals(LocalDate.now())) {
            throw new IllegalStateException("카풀(택시) 당일은 게시글을 지울 수 없습니다.");
        }

        UserEntity user = getUserByEmail(email);
        participantsRepository.deletePostParticipant(id);
        postRepository.deletePost(user.getId(), id);
        return new PostMsgDto("게시글 삭제 완료");
    }

    @Override
    @Transactional(readOnly = true)
    //@Cacheable(value = "postCache", key = "'all_posts_' + #pageable.pageNumber + '_' + #pageable.pageSize")
    public Page<PostDto> findPostList(Pageable pageable) {
        Page<Post> page = postRepository.findAllByIsDeleteYN("N", pageable);
        return page.map(Post::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    //@Cacheable(value="postCache", key="#categoryId + 'category_' + #pageable.pageNumber + '_' + #pageable.pageSize")
    public Page<PostDto> findByCategoryId(Long categoryId, Pageable pageable){
        Category category = this.categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다. " + categoryId));
        Page<Post> page = postRepository.findByCategoryAndIsDeleteYNAndPostType(category, pageable, "N", PostType.BEFORE_DEADLINE);
        return page.map(Post::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    //@Cacheable(value="postCache", key="#userId + 'userId_' + #pageable.pageNumber + '_' + #pageable.pageSize")
    public Page<PostDto> findByMemberId(Long userId, Pageable pageable){
        UserEntity user = this.userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저정보가 없습니다. 아이디: " + userId));
        Page<Post> page = postRepository.findByUserAndIsDeleteYN(user, pageable, "N");
        return page.map(Post::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostDto> findByRegion3Depth(String email, Pageable pageable){
        UserEntity user = getUserByEmail(email);
        Page<Post> page = postRepository.findByLocation(pageable, "N", user.getActivityLocation());
        return page.map(Post::toDto);
    }
    /**
     * 게시글 상세보기
     * @param id
     * @return PostDto
     */
    @Override
    @Transactional
    public PostDto showDetailPost(Long id){
        Post post = getPostById(id);
        post.setViewCount(post.getViewCount() + 1);
        postRepository.save(post);

        return post.toDto();
    }

    /**
     * 위도 & 경도로 게시글 찾기
     * @param latitude
     * @param longitude
     * @return List<PostDto>
     */
    @Override
    @Transactional(readOnly = true)
    public List<PostDto> findByLatitudeAndLongitude(Double latitude, Double longitude){
        List<Post> postList = postRepository.findByLatitudeAndLongitudeAndIsDeleteYN(latitude, longitude, "N");
        return postList.stream().map(Post::toDto).toList();
    }

    /**
     * 게시글 현재 승인 유저수
     * @param postId
     * @return ParticipateGetDto
     */
    @Override
    @Transactional(readOnly = true)
    public ParticipateGetDto getApprovalUserCountByPost(Long postId){
        Post post = getPostById(postId);
        return new ParticipateGetDto(post.getParticipantsCount(), post.getNumberOfPassengers());
    }

    /**
     * 탑승자가 운전자 매너 평가
     * @param postId
     * @param email
     * @param mannerDriverUpdateRequestDto
     * @return PostMsgDto
     */
    @Override
    @Transactional
    public PostMsgDto driverUpdateManner(Long postId, String email, MannerDriverUpdateRequestDto mannerDriverUpdateRequestDto){
        UserEntity currentUser = getUserByEmail(email);

        Post post = getPostById(postId);

        if(currentUser.getMannerCount() == null){
            currentUser.setMannerCount(0L);
        }

        if(!post.getPostType().equals(PostType.COMPLETED)){
            throw new IllegalStateException("해당 글은 완료되지 않았습니다.");
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
        // 탑승자가 운전자 후기
        notificationService.customNotify(post.getUser().getId(), post.getId()+":"+currentUser.getStudentId() + " 님이 후기를 남겼어요.", currentUser.getStudentId() + " 님이 후기를 남겼어요.", "participate");
        Alarm alarm = Alarm.builder()
                .post(post)
                .userEntity(post.getUser())
                .content(currentUser.getStudentId() + " 님이 후기를 남겼어요.")
                .createDate(LocalDateTime.now())
                .build();
        alarmRepository.save(alarm);
        this.userRepository.save(driver);

        return new PostMsgDto("운전자 평가 완료");
    }

    /**
     * 운전자가 탑승자 매너 평가
     * @param userId
     * @param mannerPassengerUpdateRequestDto
     * @param email
     * @return PostMsgDto
     */
    @Override
    @Transactional
    public PostMsgDto updateParticipatesManner(Long userId, MannerPassengerUpdateRequestDto mannerPassengerUpdateRequestDto, String email){
        UserEntity targetUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("운전자의 유저정보가 없습니다."));
        UserEntity currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("로그인 유저정보가 없습니다."));

        Participants participants = participantsRepository.findByPostIdAndUserIdAndIsDeleteYN(mannerPassengerUpdateRequestDto.getPostId(), userId, "N");

        if (Objects.equals(targetUser.getEmail(), currentUser.getEmail())) {
            throw new IllegalStateException("자기 자신을 평가할 수 없습니다.");
        }

        if(participants.getPassengerMannerFinish()){
            throw new IllegalStateException("이미 평가한 유저입니다.");
        }

        if(!participants.getPost().getPostType().equals(PostType.COMPLETED)){
            throw new IllegalStateException("해당 글은 완료되지 않았습니다.");
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
        // 운전자가 탑승자 후기
        notificationService.customNotify(targetUser.getId(), participants.getPost().getId()+":"+currentUser.getStudentId() + " 님이 후기를 남겼어요.", currentUser.getStudentId() + " 님이 후기를 남겼어요.", "participate");
        Alarm alarm = Alarm.builder()
                .post(participants.getPost())
                .userEntity(targetUser)
                .content(currentUser.getStudentId() + " 님이 후기를 남겼어요.")
                .createDate(LocalDateTime.now())
                .build();
        alarmRepository.save(alarm);
        this.userRepository.save(targetUser);

        return new PostMsgDto("탑승자 평가 완료");
    }

    private String calculateGrade(Long mannerCount) {
        String[] grades = {
            "F",   // <= -1
            "D",   // 0-9
            "D+",  // 10-19
            "C",   // 20-29
            "C+",  // 30-39`
            "B",   // 40-49
            "B+",  // 50-59
            "A",   // 60-69
            "A+",  // 70-79
            "MIO 조교님",  // 80-89
            "MIO 교수님"   // >= 90
        };

        int index = Math.min(Math.max((int) ((mannerCount + 1) / 10), 0) + 1, grades.length - 1);
        return grades[index];
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostDto> findByParticipate(String email, Pageable pageable){
        UserEntity user = this.userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("유저가 없습니다."));
        List<Participants> participants = this.participantsRepository.findByUserIdAndIsDeleteYN(user.getId(), "N");
        return new PageImpl<>(participants.stream().map(Participants::getPost).map(Post::toDto).toList(), pageable, participants.size());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostDto> findByLocation(String location) {

        if(location == null || location.isBlank()){
            throw new IllegalArgumentException("지역을 입력해주세요.");
        }

        return postRepository.findByLocationContainingAndIsDeleteYN(location, "N").stream().map(Post::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostDto> reviewsCanBeWritten(String email, Pageable pageable) {
        UserEntity user = getUserByEmail(email);

        // 탑승자로 참여한 리스트
        List<Participants> participants = this.participantsRepository.findByUserIdAndIsDeleteYN(user.getId(), "N");

        // 운전자로 참여한 리스트
        List<Participants> participants1 = this.participantsRepository.findByPostUserIdAndIsDeleteYN(user.getId(), "N");

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
    @Transactional(readOnly = true)
    public List<PostDto> findByDistance(Long postId) {
        List<Post> postList = postRepository.findByDistanceAndIsDeleteYN(postId, "N");

        return postList.stream().map(Post::toDto).toList();
    }

    //test
}
