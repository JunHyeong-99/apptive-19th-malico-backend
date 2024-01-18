package com.apptive.marico.dto.stylist;

import com.apptive.marico.entity.Stylist;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class StylistMypageDto {
    private String profile_image;
    private String nickname;

    public static StylistMypageDto toDto(Stylist stylist) {
        return StylistMypageDto.builder()
                .profile_image(stylist.getProfileImage())
                .nickname(stylist.getNickname())
                .build();
    }
}
