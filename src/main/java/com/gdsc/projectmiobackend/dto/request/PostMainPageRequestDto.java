package com.gdsc.projectmiobackend.dto.request;

import com.gdsc.projectmiobackend.common.PostType;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.annotation.Nullable;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostMainPageRequestDto {

    @Nullable
    Long categoryId;

    @Nullable
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate targetDate;

    @Nullable
    PostType postType;
}
