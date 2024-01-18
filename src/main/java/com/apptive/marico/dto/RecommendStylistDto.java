package com.apptive.marico.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecommendStylistDto {
    // 프로필 사진, 한줄 소개, 활동명
    private long stylist_id;
    private String profileImage;
    private String oneLineIntroduction;
    private String stageName;
}
