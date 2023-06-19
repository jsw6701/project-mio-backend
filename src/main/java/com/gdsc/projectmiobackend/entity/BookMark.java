package com.gdsc.projectmiobackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookMark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn
    private Post post;

    @ManyToOne
    @JoinColumn
    private UserEntity userEntity;

    private Boolean status;

    public BookMark(Post post, UserEntity userEntity, boolean status) {
        this.post = post;
        this.userEntity = userEntity;
        this.status = status;
    }
}
