package com.gdsc.projectmiobackend.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MannerDriverUpdateRequestDto {
    private String manner;

    private String content;
}
