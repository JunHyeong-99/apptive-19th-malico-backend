package com.apptive.marico.dto.stylist;


import com.apptive.marico.entity.Style;
import com.apptive.marico.entity.Stylist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
public class FilterStyleDto {
    private Long style_id;
    private String img;


    public static FilterStyleDto toDto(Style style) {
        return FilterStyleDto.builder()
                .style_id(style.getId())
                .img(style.getImage())
                .build();
    }
    @Builder
    @Getter
    public static class DtoList {
        private List<StyleDto> styleDtoList = new ArrayList<>();
    }


}