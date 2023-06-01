package com.gdsc.projectmiobackend.dto.request;

import lombok.*;


@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AdditionalUserPatchDto {

    //성별
    private Boolean gender;

    //흡연여부
    private Boolean verifySmoker;

    //계좌번호
    private String accountNumber;
}
