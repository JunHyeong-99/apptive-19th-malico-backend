package com.apptive.marico.dto.styling;

import com.apptive.marico.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MyClientMemberDto {
    private long member_id;
    private String profileImage;
    private String nickname;

    static public MyClientMemberDto toDto(Member member) {
        return MyClientMemberDto.builder()
                .member_id(member.getId())
                .profileImage(member.getProfileImage())
                .nickname(member.getNickname())
                .build();
    }
}
