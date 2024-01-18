package com.apptive.marico.dto.token;


import lombok.Builder;
import lombok.Getter;


@Builder
@Getter
public class TokenResponseDto {
    private String grantType;
    private String accessToken;
    private Long accessTokenExpiresIn;
}
