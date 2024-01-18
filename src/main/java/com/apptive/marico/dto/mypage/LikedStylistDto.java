package com.apptive.marico.dto.mypage;

import com.apptive.marico.dto.member.MemberMypageDto;
import com.apptive.marico.entity.Member;
import com.apptive.marico.entity.Stylist;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LikedStylistDto {
    private String nickname;
    private String profile_image;

    public static LikedStylistDto toDto(Stylist stylist) {
        return LikedStylistDto.builder()
                .nickname(stylist.getNickname())
                .profile_image(stylist.getProfileImage())
                .build();
    }
}
