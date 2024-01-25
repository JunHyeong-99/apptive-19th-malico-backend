package com.apptive.marico.dto.stylist;


import com.apptive.marico.dto.CareerDto;
import com.apptive.marico.entity.Career;
import com.apptive.marico.entity.City;
import com.apptive.marico.entity.Stylist;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@Setter
public class StylistMypageEditDto {
    private String profile_image;
    private String stageName;
    private String oneLineIntroduction;
    private String stylistIntroduction;
    private String city;
    private String state;
    private String chat_link;
    private List<CareerDto> careerDtoList;

    public static StylistMypageEditDto toDto(Stylist stylist) {
        return StylistMypageEditDto.builder()
                .profile_image(stylist.getProfileImage())
                .stageName(stylist.getStageName())
                .oneLineIntroduction(stylist.getOneLineIntroduction())
                .stylistIntroduction(stylist.getStylistIntroduction())
                .city(stylist.getCity().getDisplayName())
                .state(stylist.getState())
                .chat_link(stylist.getChat_link())
                .careerDtoList(stylist.getCareer().stream()
                        .map(CareerDto::toDto)
                        .collect(Collectors.toList()))
                .build();
    }
}
