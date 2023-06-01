package com.gdsc.projectmiobackend.dto;

import com.gdsc.projectmiobackend.common.RoleType;
import com.gdsc.projectmiobackend.common.Status;
import com.gdsc.projectmiobackend.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String email;
    private String studentId;
    private String profileImageUrl;  // 파일 저장 경로
    private String name;
    private Boolean gender;
    private String accountNumber;
    private Boolean verifySmoker;
    private RoleType roleType;
    private Status status;

    public UserDto(UserEntity user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.studentId = user.getStudentId();
        this.profileImageUrl = user.getProfileImageUrl();
        this.name = user.getName();
        this.gender = user.getGender();
        this.accountNumber = user.getAccountNumber();
        this.verifySmoker = user.getVerifySmoker();
        this.roleType = user.getRoleType();
        this.status = user.getStatus();
    }
}
