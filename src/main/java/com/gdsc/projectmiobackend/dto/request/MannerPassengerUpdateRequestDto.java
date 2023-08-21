package com.gdsc.projectmiobackend.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MannerPassengerUpdateRequestDto {
    private String manner;

    private String content;

    private Long postId;
}

