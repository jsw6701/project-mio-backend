package com.gdsc.projectmiobackend.dto;

import com.gdsc.projectmiobackend.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CategoryDto {

    private Long categoryId;

    private String categoryName;

    public CategoryDto(Category category){
        this.categoryId = category.getCategoryId();
        this.categoryName = category.getCategoryName();
    }
}
