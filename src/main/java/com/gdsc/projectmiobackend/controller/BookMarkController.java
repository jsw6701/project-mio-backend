package com.gdsc.projectmiobackend.controller;

import com.gdsc.projectmiobackend.entity.BookMark;
import com.gdsc.projectmiobackend.jwt.dto.UserInfo;
import com.gdsc.projectmiobackend.service.BookMarkService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/bookmark")
public class BookMarkController {

    private final BookMarkService bookMarkService;

    @Operation(summary = "북마크 추가")
    @PostMapping("{postId}")
    public ResponseEntity<?> addBookMark(Long postId,
                                            @AuthenticationPrincipal UserInfo user){

        this.bookMarkService.saveBookMark(postId, user.getEmail());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "회원별 북마크 조회")
    @GetMapping("/user")
    public ResponseEntity<List<BookMark>> readBookMark(@AuthenticationPrincipal UserInfo user){
        List<BookMark> bookMarks = this.bookMarkService.getUserBookMarkList(user.getEmail());
        return ResponseEntity.ok(bookMarks);
    }

}
