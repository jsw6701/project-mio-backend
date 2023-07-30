package com.gdsc.projectmiobackend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class ParticipateDto {
    private Long postId;
    private Long userId;
    private String content;

    public ParticipateDto(Long postId, Long userId, String content){
        this.postId = postId;
        this.userId = userId;
        this.content = content;
    }
}
