package com.apptive.marico.dto.stylist;

import com.apptive.marico.entity.Member;
import com.apptive.marico.entity.Stylist;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StylistResponseDto {
    private String email;
    private String nickname;

// 코드의 재사용성과 가독성을 위한 Member 객체를 Dto 객체로 변환하는 일종의 캡슐화 전략
    public static StylistResponseDto toDto(Stylist stylist) {
        return StylistResponseDto.builder()
                .email(stylist.getUsername())
                .nickname(stylist.getNickname()).build();
    }

}
