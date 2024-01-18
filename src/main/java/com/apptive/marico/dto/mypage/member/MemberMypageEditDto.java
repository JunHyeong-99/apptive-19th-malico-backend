package com.apptive.marico.dto.mypage.member;


import com.apptive.marico.entity.Member;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberMypageEditDto {
    private String nickname;
    private char gender;
    private String email;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    public static MemberMypageEditDto toDto(Member member) {
        return MemberMypageEditDto.builder()
                .nickname(member.getNickname())
                .gender(member.getGender())
                .email(member.getEmail())
                .birthDate(member.getBirthDate())
                .build();
    }
}
