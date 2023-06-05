package com.gdsc.projectmiobackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CategoryPatchRequestDto {

    @Schema(description = "카테고리 이름.", example = "카테고리 이름")
    @NotEmpty(message = "카테고리는 필수 항목입니다.")
    private String categoryName;
}
