package com.gdsc.projectmiobackend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Alarm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createDate;

    private String content;

    @ManyToOne
    private Post post;

    @ManyToOne
    private UserEntity userEntity;

    public Alarm(LocalDateTime createDate, String content, Post post, UserEntity userEntity) {
        this.createDate = createDate;
        this.content = content;
        this.post = post;
        this.userEntity = userEntity;
    }
}
