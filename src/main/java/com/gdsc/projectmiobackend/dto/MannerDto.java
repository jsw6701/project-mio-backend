package com.gdsc.projectmiobackend.dto;

import com.gdsc.projectmiobackend.common.Manner;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class MannerDto {

    private Long id;
    private Manner manner;
    private String content;
    private Long getUserId;
    private Long postUserId;
    private String createDate;


    public MannerDto(Long id, Manner manner, String content, Long postUserId, Long getUserId, String createDate) {
        this.id = id;
        this.manner = manner;
        this.content = content;
        this.getUserId = getUserId;
        this.postUserId = postUserId;
        this.createDate = createDate;
    }
}
