package com.gdsc.projectmiobackend.controller;


import com.gdsc.projectmiobackend.dto.CategoryDto;
import com.gdsc.projectmiobackend.dto.request.CategoryCreateRequestDto;
import com.gdsc.projectmiobackend.dto.request.CategoryPatchRequestDto;
import com.gdsc.projectmiobackend.entity.Category;
import com.gdsc.projectmiobackend.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("category")
    public ResponseEntity<CategoryDto> create(@RequestBody CategoryCreateRequestDto createRequestDto){
        System.out.println("create");

        Category result = this.categoryService.addCategory(createRequestDto);
        return ResponseEntity.ok(new CategoryDto(result));
    }

    @GetMapping("/category/read")
    public ResponseEntity<String> findNameById(Long id){
        System.out.println("find name");
        Category category = this.categoryService.findById(id);
        return ResponseEntity.ok(category.getCategoryName());
    }

    @GetMapping("/category")
    public ResponseEntity<List<CategoryDto>> readAll(){
        System.out.println("read all");
        List<CategoryDto> categoryList = this.categoryService.findAllCategory();
        return ResponseEntity.ok(categoryList);
    }

    @PatchMapping("category/{id}")
    public ResponseEntity<CategoryDto> update(@PathVariable Long id, @RequestBody CategoryPatchRequestDto patchRequestDto){
        System.out.println("update");

        Category result = categoryService.updateById(id, patchRequestDto);
        return ResponseEntity.ok(new CategoryDto(result));
    }

    @DeleteMapping("category/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        System.out.println("delete");

        this.categoryService.deleteCategory(id);
        return ResponseEntity.ok().build();
    }
}
