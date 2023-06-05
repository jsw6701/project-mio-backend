package com.gdsc.projectmiobackend.controller;


import com.gdsc.projectmiobackend.dto.CategoryDto;
import com.gdsc.projectmiobackend.dto.request.CategoryCreateRequestDto;
import com.gdsc.projectmiobackend.dto.request.CategoryPatchRequestDto;
import com.gdsc.projectmiobackend.entity.Category;
import com.gdsc.projectmiobackend.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@Tag(name = "카테고리")
public class CategoryController {

    private final CategoryService categoryService;


    @Operation(summary = "카테고리 생성")
    @PostMapping("category")
    public ResponseEntity<CategoryDto> create(@RequestBody CategoryCreateRequestDto createRequestDto){
        System.out.println("create");

        Category result = this.categoryService.addCategory(createRequestDto);
        return ResponseEntity.ok(new CategoryDto(result));
    }

    @Operation(summary = "카테고리 이름 조회")
    @GetMapping("/category/read")
    public ResponseEntity<String> findNameById(Long id){
        System.out.println("find name");
        Category category = this.categoryService.findById(id);
        return ResponseEntity.ok(category.getCategoryName());
    }

    @Operation(summary = "카테고리 전체 조회")
    @GetMapping("/category")
    public ResponseEntity<List<CategoryDto>> readAll(){
        System.out.println("read all");
        List<CategoryDto> categoryList = this.categoryService.findAllCategory();
        return ResponseEntity.ok(categoryList);
    }

    @Operation(summary = "카테고리 수정")
    @PatchMapping("category/{id}")
    public ResponseEntity<CategoryDto> update(@PathVariable Long id, @RequestBody CategoryPatchRequestDto patchRequestDto){
        System.out.println("update");

        Category result = categoryService.updateById(id, patchRequestDto);
        return ResponseEntity.ok(new CategoryDto(result));
    }

    @Operation(summary = "카테고리 삭제")
    @DeleteMapping("category/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        System.out.println("delete");

        this.categoryService.deleteCategory(id);
        return ResponseEntity.ok().build();
    }
}
