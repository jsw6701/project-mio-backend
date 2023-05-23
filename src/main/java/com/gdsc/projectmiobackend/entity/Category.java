package com.gdsc.projectmiobackend.entity;

import com.gdsc.projectmiobackend.dto.CategoryDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@ToString
@Table
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // 카테고리 아이디
    private Long categoryId;

    // 카테고리 이름
    private String categoryName;

    public CategoryDto toDto() {
        return CategoryDto.builder()
                .categoryId(categoryId)
                .categoryName(categoryName)
                .build();
    }
}
