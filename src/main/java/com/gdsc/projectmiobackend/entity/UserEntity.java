package com.gdsc.projectmiobackend.entity;


import com.gdsc.projectmiobackend.common.RoleType;
import com.gdsc.projectmiobackend.common.SocialType;
import com.gdsc.projectmiobackend.common.Status;
import com.gdsc.projectmiobackend.oauth.GoogleOAuth2UserInfo;
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

    private String profileImageUrl;  // 파일 저장 경로

    private String name;

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

    public UserEntity(GoogleOAuth2UserInfo userInfo) {
        this.email = userInfo.getEmail();
        this.profileImageUrl = userInfo.getImageUrl();
        this.name = userInfo.getName();
        this.roleType = RoleType.MEMBER;
        this.status = Status.ACTIVE;
    }
}
