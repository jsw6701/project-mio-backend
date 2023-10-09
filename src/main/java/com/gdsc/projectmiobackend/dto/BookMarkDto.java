package com.gdsc.projectmiobackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookMarkDto {

    private Long id;
    private Long postId;
    private Long userId;
    private Boolean status;
}
