package com.apptive.marico.dto.stylist;

import com.apptive.marico.dto.CareerDto;
import com.apptive.marico.dto.stylist.service.StylistServiceDto;
import com.apptive.marico.entity.Stylist;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
public class StylistDetailDto {
    private String profileImage;
    private String stageName;
    private String oneLineIntroduction;
    private String stylistIntroduction;
    private String city;
    private String state;
    private List<StyleDto> styleDtoList;
    private List<CareerDto> careerDtoList;
    private List<StylistServiceDto> stylistServiceDtoList;

    public static StylistDetailDto toDto(Stylist stylist) {
        return StylistDetailDto.builder()
                .profileImage(stylist.getProfileImage())
                .stageName(stylist.getStageName())
                .oneLineIntroduction(stylist.getOneLineIntroduction())
                .stylistIntroduction(stylist.getStylistIntroduction())
                .city(stylist.getCity())
                .state(stylist.getState())
                .styleDtoList(stylist.getStyles().stream()
                        .map(StyleDto::toDto)
                        .collect(Collectors.toList()))
                .careerDtoList(stylist.getCareer().stream()
                        .map(CareerDto::toDto)
                        .collect(Collectors.toList()))
                .stylistServiceDtoList(stylist.getStylistServices().stream()
                        .map(StylistServiceDto::toDto)
                        .collect(Collectors.toList()))
                .build();
    }
}
