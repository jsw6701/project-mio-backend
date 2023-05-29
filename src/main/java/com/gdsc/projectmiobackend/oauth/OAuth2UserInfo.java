package com.gdsc.projectmiobackend.oauth;

public abstract class OAuth2UserInfo {
    public abstract String getId();

    public abstract String getName();

    public abstract String getEmail();

    public abstract String getImageUrl();
}
