package com.gdsc.projectmiobackend.dto.request;

import com.gdsc.projectmiobackend.entity.Category;
import com.gdsc.projectmiobackend.entity.Post;
import com.gdsc.projectmiobackend.entity.UserEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostCreateRequestDto {

    @Schema(description = "제목입니다.", example = "제목")
    @NotEmpty(message="제목은 필수 항목입니다.")
    @Size(max=200)
    private String title;

    @Schema(description = "내용입니다.", example = "내용")
    @NotEmpty(message="내용은 필수 항목입니다.")
    private String content;

    @Nullable
    private MultipartFile file;

    @Nullable
    private String fileName;

    @Nullable
    private String filePath;

    @Nullable
    private Long viewCount;


    public Post toEntity(Category category, UserEntity user) {

        return Post.builder()
                .title(title)
                .content(content)
                .createDate(LocalDateTime.now())
                .category(category)
                .fileName(fileName)
                .filePath(filePath)
                .viewCount(viewCount)
                .user(user)
                .build();
    }
}
