package com.apptive.marico.dto;


import com.apptive.marico.entity.Career;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CareerDto {
    private String organizationName;
    // 학과
    private String content;
    private String startYear;
    private String endYear;
    public static CareerDto toDto(Career career) {
        return CareerDto.builder()
                .organizationName(career.getOrganizationName())
                .content(career.getContent())
                .startYear(career.getStartYear())
                .endYear(career.getEndYear())
                .build();
    }
}
