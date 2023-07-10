package com.gdsc.projectmiobackend.dto;

import com.gdsc.projectmiobackend.entity.Participants;
import com.gdsc.projectmiobackend.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParticipateDto {
    private UserEntity user;
    private String content;


    public ParticipateDto(Participants participants){
        this.user = participants.getUser();
        this.content = participants.getContent();
    }
}
