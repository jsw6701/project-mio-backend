package com.gdsc.projectmiobackend.service;

import com.gdsc.projectmiobackend.dto.CategoryDto;
import com.gdsc.projectmiobackend.dto.request.CategoryCreateRequestDto;
import com.gdsc.projectmiobackend.dto.request.CategoryPatchRequestDto;
import com.gdsc.projectmiobackend.entity.Category;
import com.gdsc.projectmiobackend.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService{
    private final CategoryRepository categoryRepository;

    @Override
    public Category findById(Long id) {
        return categoryRepository.findById(id).orElse(new Category());
    }

    @Override
    public Category addCategory(CategoryCreateRequestDto categoryCreateRequestDto) {
        return categoryRepository.save(categoryCreateRequestDto.toEntity());
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public Category updateById(Long id, CategoryPatchRequestDto categoryPatchRequestDto){
        Category category = this.findById(id);
        category.setCategoryName(categoryPatchRequestDto.getCategoryName());
        return this.categoryRepository.save(category);
    }


    @Override
    public List<CategoryDto> findAllCategory() {
        List<Category> categorysList = categoryRepository.findAll();
        return categorysList.stream().map(Category::toDto).toList();
    }
}
