package com.gdsc.projectmiobackend.controller;


import com.gdsc.projectmiobackend.dto.PostDto;
import com.gdsc.projectmiobackend.dto.request.PostCreateRequestDto;
import com.gdsc.projectmiobackend.dto.request.PostPatchRequestDto;
import com.gdsc.projectmiobackend.entity.Post;
import com.gdsc.projectmiobackend.entity.UserEntity;
import com.gdsc.projectmiobackend.jwt.dto.UserInfo;
import com.gdsc.projectmiobackend.service.PostService;
import lombok.AllArgsConstructor;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.UUID;


@AllArgsConstructor
@RestController
public class PostController {

    private final PostService postService;

    @PostMapping(value = "post/{categoryId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<PostDto> create(
            @ModelAttribute PostCreateRequestDto postCreateRequestDto,
            @PathVariable Long categoryId,
            @AuthenticationPrincipal UserInfo user) throws Exception{
        System.out.println("create");

        postCreateRequestDto.setViewCount(0L);

        MultipartFile file = postCreateRequestDto.getFile();

        if(file != null){
            String projectPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files";

            UUID uuid = UUID.randomUUID();

            String fileName = uuid + "_" + file.getOriginalFilename();

            File saveFile = new File(projectPath, fileName);

            file.transferTo(saveFile);

            postCreateRequestDto.setFileName(fileName);
            postCreateRequestDto.setFilePath("/files/" + fileName);
        }

        Post post = this.postService.addPostList(postCreateRequestDto, categoryId, user.getEmail());
        return ResponseEntity.ok(new PostDto(post));
    }

    @PatchMapping("post/{id}")
    public ResponseEntity<PostDto> update(
            @PathVariable Long id,
            @RequestBody PostPatchRequestDto patchRequestDto,
            @AuthenticationPrincipal UserInfo user){
        System.out.println("update");

        Post post = postService.updateById(id, patchRequestDto, user.getEmail());
        return ResponseEntity.ok(new PostDto(post));
    }

    @DeleteMapping("post/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, @AuthenticationPrincipal UserInfo user){
        System.out.println("delete");

        this.postService.deletePostList(id, user.getEmail());
        return ResponseEntity.ok().build();
    }

    @PageableAsQueryParam
    @GetMapping("/readAll")
    public ResponseEntity<Page<PostDto>> readAll(@PageableDefault(sort = "createDate", direction = Sort.Direction.DESC) Pageable pageable){
        System.out.println("read all");

        Page<PostDto> postList = this.postService.findPostList(pageable);

        return ResponseEntity.ok(postList);
    }

    @PageableAsQueryParam
    @GetMapping("/viewCount")
    public ResponseEntity<Page<PostDto>> readAllByViewCount(@PageableDefault(sort = "viewCount", direction = Sort.Direction.DESC) Pageable pageable){
        System.out.println("read all");

        Page<PostDto> postList = this.postService.findPostList(pageable);

        return ResponseEntity.ok(postList);
    }

    @PageableAsQueryParam
    @GetMapping("categoryPost/{categoryId}")
    public ResponseEntity<Page<PostDto>> readPostsByCategory(
            @PageableDefault(sort = "createDate", direction = Sort.Direction.DESC) Pageable pageable,
            @PathVariable Long categoryId){
        System.out.println("Posts by category");

        Page<PostDto> postsByCategoryList = this.postService.findByCategoryId(categoryId, pageable);

        return ResponseEntity.ok(postsByCategoryList);
    }

    @PageableAsQueryParam
    @GetMapping("memberPost/{userId}")
    public ResponseEntity<Page<PostDto>> readPostsByUser(
            @PageableDefault(sort = "createDate", direction = Sort.Direction.DESC) Pageable pageable,
            @PathVariable Long userId){
        System.out.println("Posts by member");

        Page<PostDto> postsByMemberList = this.postService.findByMemberId(userId, pageable);

        return ResponseEntity.ok(postsByMemberList);
    }

    //상세페이지
    @GetMapping("detail/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Long id){
        Post post = postService.showDetailPost(id);
        return ResponseEntity.ok(new PostDto(post));
    }

    @PostMapping("/{postId}/participate")
    public ResponseEntity<Void> participateInPost(@PathVariable Long postId, @AuthenticationPrincipal UserInfo user) {
        postService.participateInPost(postId, user.getEmail());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{postId}/participants")
    public ResponseEntity<List<UserEntity>> getParticipantsByPostId(@PathVariable Long postId) {
        List<UserEntity> participants = postService.getParticipantsByPostId(postId);
        return new ResponseEntity<>(participants, HttpStatus.OK);
    }

    @PageableAsQueryParam
    @GetMapping("/user/participants")
    public ResponseEntity<Page<PostDto>> getParticipantsByUserId(@AuthenticationPrincipal UserInfo user,
                                                              @PageableDefault(sort = "targetDate", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PostDto> posts = postService.getPostIdsByUserEmail(user.getEmail(), pageable);
        return ResponseEntity.ok(posts);
    }
}
