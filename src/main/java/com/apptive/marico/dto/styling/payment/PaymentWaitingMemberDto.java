package com.apptive.marico.dto.styling.payment;

import com.apptive.marico.entity.Member;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentWaitingMemberDto {
    private long serviceApplicationId;
    private String profileImage; // 필수
    private String nickname; // 필수


    static public PaymentWaitingMemberDto toDto(Long id, Member member){
        return PaymentWaitingMemberDto.builder()
                .serviceApplicationId(id)
                .profileImage(member.getProfileImage())
                .nickname(member.getNickname())
                .build();
    }

}
