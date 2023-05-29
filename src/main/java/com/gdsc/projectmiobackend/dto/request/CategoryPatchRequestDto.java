package com.gdsc.projectmiobackend.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CategoryPatchRequestDto {

    private Long categoryId;

    private String categoryName;
}
