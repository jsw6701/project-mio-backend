package com.gdsc.projectmiobackend.entity;

import com.gdsc.projectmiobackend.dto.MannerDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MannerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String manner;

    private String content;

    private Long getUserId;

    private Long postUserId;

    private LocalDateTime createDate;

    public MannerEntity(String manner, String content, Long getUserId, Long postUserId, LocalDateTime createDate) {
        this.manner = manner;
        this.content = content;
        this.getUserId = getUserId;
        this.postUserId = postUserId;
        this.createDate = createDate;
    }

    public MannerDto toDto() {
        return MannerDto.builder()
                .id(id)
                .manner(manner)
                .content(content)
                .getUserId(getUserId)
                .createDate(createDate.toString())
                .build();
    }
}
