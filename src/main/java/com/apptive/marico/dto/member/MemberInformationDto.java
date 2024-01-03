package com.apptive.marico.dto.member;

import com.apptive.marico.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MemberInformationDto {
    private String nickname;
    private char gender;
    private String email;

    public static MemberInformationDto toDto(Member member) {
        return MemberInformationDto.builder()
                .nickname(member.getNickname())
                .gender(member.getGender())
                .email(member.getEmail())
                .build();
    }
}
