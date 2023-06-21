package com.gdsc.projectmiobackend.dto;

import com.gdsc.projectmiobackend.entity.Alarm;
import com.gdsc.projectmiobackend.entity.Post;
import com.gdsc.projectmiobackend.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlarmDto {
    private Long id;
    private LocalDateTime createDate;
    private String content;
    private Post post;
    private UserEntity userEntity;

    public AlarmDto(Alarm alarm){
        this.id = alarm.getId();
        this.createDate = alarm.getCreateDate();
        this.content = alarm.getContent();
        this.post = alarm.getPost();
        this.userEntity = alarm.getUserEntity();
    }
}
