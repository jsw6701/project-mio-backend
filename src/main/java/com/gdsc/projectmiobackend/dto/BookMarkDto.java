package com.gdsc.projectmiobackend.dto;

import com.gdsc.projectmiobackend.entity.Post;
import com.gdsc.projectmiobackend.entity.UserEntity;
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
    private Post post;
    private UserEntity userEntity;
    private Boolean status;
}
