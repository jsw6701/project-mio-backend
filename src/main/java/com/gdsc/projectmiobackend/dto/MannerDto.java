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
    private Long userId;
    private String createDate;


    public MannerDto(Long id, String manner, String content, Long userId, String createDate) {
        this.id = id;
        this.manner = manner;
        this.content = content;
        this.userId = userId;
        this.createDate = createDate;
    }
}
