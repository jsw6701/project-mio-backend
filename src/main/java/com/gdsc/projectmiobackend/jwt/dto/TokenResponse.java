package com.gdsc.projectmiobackend.jwt.dto;


public record TokenResponse(
         String grantType,
         String accessToken,
         String refreshToken,
         Long accessTokenExpiresIn)
{ }
