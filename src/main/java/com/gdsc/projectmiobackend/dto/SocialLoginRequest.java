package com.gdsc.projectmiobackend.dto;



public record SocialLoginRequest(
        String token,
        String url,
        String method
) { }
