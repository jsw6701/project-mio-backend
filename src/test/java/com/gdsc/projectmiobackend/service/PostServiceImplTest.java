package com.gdsc.projectmiobackend.service;

import com.gdsc.projectmiobackend.dto.PostDto;
import com.gdsc.projectmiobackend.dto.request.PostCreateRequestDto;
import com.gdsc.projectmiobackend.entity.Category;
import com.gdsc.projectmiobackend.entity.Participants;
import com.gdsc.projectmiobackend.entity.Post;
import com.gdsc.projectmiobackend.entity.UserEntity;
import com.gdsc.projectmiobackend.repository.CategoryRepository;
import com.gdsc.projectmiobackend.repository.ParticipantsRepository;
import com.gdsc.projectmiobackend.repository.PostRepository;
import com.gdsc.projectmiobackend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ParticipantsRepository participantsRepository;

    @InjectMocks
    private PostServiceImpl postService;

    private UserEntity user;
    private Category category;
    private Post post;
    private Participants participants;

    @BeforeEach
    void setUp() {
        user = createUser("test@example.com", 1L);
        category = createCategory(1L);
        post = createPost(1L, "Test Title", "Test Content");
        participants = createParticipants(post, user);
    }

    private UserEntity createUser(String email, Long id) {
        UserEntity user = new UserEntity();
        user.setId(id);
        user.setEmail(email);
        return user;
    }

    private Category createCategory(Long id) {
        Category category = new Category();
        category.setCategoryId(id);
        return category;
    }

    private Post createPost(Long id, String title, String content) {
        Post post = new Post();
        post.setId(id);
        post.setTitle(title);
        post.setContent(content);
        return post;
    }

    private Participants createParticipants(Post post, UserEntity user) {
        Participants participants = new Participants();
        participants.setPost(post);
        participants.setUser(user);
        return participants;
    }

    @Test
    @DisplayName("게시글 추가_성공")
    void addPost_Success() {
        // Given

        PostCreateRequestDto requestDto = new PostCreateRequestDto();
        requestDto.setTitle("Test Title");
        requestDto.setContent("Test Content");

        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));
        given(categoryRepository.findById(category.getCategoryId())).willReturn(Optional.of(category));
        given(postRepository.save(any(Post.class))).willReturn(post);
        given(participantsRepository.save(any(Participants.class))).willReturn(participants);

        // When
        PostDto result = postService.addPost(requestDto, category.getCategoryId(), user.getEmail());

        // Then
        assertNotNull(result);
        assertEquals("Test Title", result.getTitle());
        assertEquals("Test Content", result.getContent());
    }

    @Test
    @DisplayName("게시글 실패_유저를 찾을 수 없는 경우")
    void addPost_UserNotFound() {
        // Given
        PostCreateRequestDto requestDto = new PostCreateRequestDto();

        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> postService.addPost(requestDto, category.getCategoryId(), user.getEmail()));
    }

    @Test
    @DisplayName("게시글 실패_카테고리를 찾을 수 없는 경우")
    void addPost_CategoryNotFound() {
        // Given
        PostCreateRequestDto requestDto = new PostCreateRequestDto();

        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));
        given(categoryRepository.findById(category.getCategoryId())).willReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> postService.addPost(requestDto, category.getCategoryId(), user.getEmail()));
    }

/*    @Test
    @DisplayName("게시글 삭제_성공")
    void deletePost_Success() {
        // Given
        post.setPostType(PostType.BEFORE_DEADLINE);
        post.setTargetDate(LocalDate.now().plusDays(1));
        post.setIsDeleteYN("N");

        given(postRepository.findById(post.getId())).willReturn(Optional.of(post));
        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));

        // When
        postService.deletePostList(post.getId(), user.getEmail());

        // Then
        assertEquals("Y", post.getIsDeleteYN());
    }*/
}