package com.gdsc.projectmiobackend.service;


import com.gdsc.projectmiobackend.dto.PostDto;
import com.gdsc.projectmiobackend.dto.request.PostCreateRequestDto;
import com.gdsc.projectmiobackend.dto.request.PostPatchRequestDto;
import com.gdsc.projectmiobackend.dto.request.PostVerifyFinishRequestDto;
import com.gdsc.projectmiobackend.entity.Category;
import com.gdsc.projectmiobackend.entity.Post;
import com.gdsc.projectmiobackend.entity.UserEntity;
import com.gdsc.projectmiobackend.repository.CategoryRepository;
import com.gdsc.projectmiobackend.repository.PostRepository;
import com.gdsc.projectmiobackend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService{

    private final PostRepository postRepository;

    private final CategoryRepository categoryRepository;

    private final UserRepository userRepository;


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
        UserEntity user = this.userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("카테고리가 없습니다."));
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
}
