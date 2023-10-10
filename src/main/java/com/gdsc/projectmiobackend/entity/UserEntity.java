package com.gdsc.projectmiobackend.entity;


import com.gdsc.projectmiobackend.common.RoleType;
import com.gdsc.projectmiobackend.common.Status;
import com.gdsc.projectmiobackend.dto.UserDto;
import com.gdsc.projectmiobackend.oauth.GoogleOAuth2UserInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@Entity @Table(name = "tb_user")
public class UserEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    private String studentId;

    private String profileImageUrl;  // 파일 저장 경로

    private String name;

    @Nullable
    @Schema(description = "계좌번호입니다.", example = "국민 123456-78-901234")
    private String accountNumber;

    //성별
    @Nullable
    private Boolean gender;

    //흡연여부
    @Nullable
    private Boolean verifySmoker;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @Enumerated(EnumType.STRING)
    private Status status;

    private Long mannerCount;

    private String grade;

    private String activityLocation;

    public UserEntity(GoogleOAuth2UserInfo userInfo) {
        this.email = userInfo.getEmail();
        this.studentId = userInfo.getEmail().split("@")[0];
        this.profileImageUrl = userInfo.getImageUrl();
        this.name = userInfo.getName();
        this.roleType = RoleType.MEMBER;
        this.status = Status.ACTIVE;
    }

    public UserDto toDto(){
        return UserDto.builder()
                .id(id)
                .email(email)
                .studentId(studentId)
                .profileImageUrl(profileImageUrl)
                .name(name)
                .accountNumber(accountNumber)
                .activityLocation(activityLocation)
                .gender(gender)
                .grade(grade)
                .mannerCount(mannerCount)
                .roleType(roleType)
                .status(status)
                .verifySmoker(verifySmoker)
                .build();
    }
}
