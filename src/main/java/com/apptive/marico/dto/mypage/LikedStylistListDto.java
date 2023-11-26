package com.apptive.marico.dto.mypage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
@Builder
@Getter
@AllArgsConstructor
public class LikedStylistListDto {
    private List<LikedStylistDto> likedStylists;

    public static LikedStylistListDto toDto(List<LikedStylistDto> likedStylists) {
        return LikedStylistListDto.builder()
                .likedStylists(likedStylists)
                .build();
    }
}
