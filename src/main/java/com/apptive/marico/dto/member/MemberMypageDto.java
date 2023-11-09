package com.apptive.marico.dto.member;

import com.apptive.marico.entity.Member;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

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
