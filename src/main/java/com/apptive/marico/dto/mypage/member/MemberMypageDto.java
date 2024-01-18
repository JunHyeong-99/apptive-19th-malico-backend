package com.apptive.marico.dto.mypage.member;

import com.apptive.marico.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MemberMypageDto {
    private String profile_image;
    private String nickname;

    public static MemberMypageDto toDto(Member member) {
        return MemberMypageDto.builder()
                .profile_image(member.getProfileImage())
                .nickname(member.getNickname())
                .build();
    }
}
