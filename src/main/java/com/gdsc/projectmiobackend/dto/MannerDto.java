package com.gdsc.projectmiobackend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class MannerDto {

    private Long id;
    private String manner;
    private String content;
    private Long getUserId;
    private Long postUserId;
    private String createDate;


    public MannerDto(Long id, String manner, String content, Long postUserId, Long getUserId, String createDate) {
        this.id = id;
        this.manner = manner;
        this.content = content;
        this.getUserId = getUserId;
        this.postUserId = postUserId;
        this.createDate = createDate;
    }
}
