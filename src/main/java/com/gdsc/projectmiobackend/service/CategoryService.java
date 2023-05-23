package com.gdsc.projectmiobackend.service;

import com.gdsc.projectmiobackend.dto.CategoryDto;
import com.gdsc.projectmiobackend.dto.request.CategoryCreateRequestDto;
import com.gdsc.projectmiobackend.dto.request.CategoryPatchRequestDto;
import com.gdsc.projectmiobackend.entity.Category;

import java.util.List;

public interface CategoryService {

    Category findById(Long id);

    Category addCategory(CategoryCreateRequestDto categoryCreateRequestDto);

    Category updateById(Long id, CategoryPatchRequestDto categoryPatchRequestDto);

    void deleteCategory(Long id);

    List<CategoryDto> findAllCategory();
}
