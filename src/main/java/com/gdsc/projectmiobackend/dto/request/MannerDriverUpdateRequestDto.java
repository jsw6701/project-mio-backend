package com.gdsc.projectmiobackend.dto.request;

import com.gdsc.projectmiobackend.common.Manner;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MannerDriverUpdateRequestDto {
    private Manner manner;
    private String content;
}
