package com.gdsc.projectmiobackend.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostPatchRequestDto {
    @NotEmpty(message="내용은 필수 항목입니다.")
    private String content;

    private Long categoryId;
}
