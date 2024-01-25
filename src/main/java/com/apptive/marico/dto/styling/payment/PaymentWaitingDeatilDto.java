package com.apptive.marico.dto.styling.payment;

import com.apptive.marico.entity.Member;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentWaitingDeatilDto {
    private long serviceApplicationId;
    private String profileImage;
    private String nickname;

    private String connectionType;
    private String serviceType;

    private List<String> preferredStyleCategories;

    private String city;
    private String state;


    static public PaymentWaitingDeatilDto toDto(Long id, Member member, List<String> preferredStyleCategories){
        return PaymentWaitingDeatilDto.builder()
                .serviceApplicationId(id)
                .profileImage(member.getProfileImage())
                .nickname(member.getNickname())
                .connectionType(null)
                .serviceType(null)
                .preferredStyleCategories(preferredStyleCategories)
                .city(member.getCity().getDisplayName())
                .state(member.getState())
                .build();
    }
}
