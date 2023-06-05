package com.gdsc.projectmiobackend.dto.request;

import com.gdsc.projectmiobackend.entity.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryCreateRequestDto {

    @Schema(description = "카테고리 이름.", example = "카테고리 이름")
    @NotEmpty(message = "카테고리는 필수 항목입니다.")
    private String categoryName;

    public Category toEntity(){
        return Category.builder()
                .categoryName(categoryName)
                .build();
    }
}
