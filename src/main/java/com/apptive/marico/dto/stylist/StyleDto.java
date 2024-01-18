package com.apptive.marico.dto.stylist;

import com.apptive.marico.entity.Style;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
public class StyleDto {

    private Long style_id;
    private String image;
    private String category;

    public static StyleDto toDto(Style style) {
        return StyleDto.builder()
                .style_id(style.getId())
                .image(style.getImage())
                .category(style.getCategory())
                .build();
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class DtoList {
        private List<StyleDto> styleDtoList = new ArrayList<>();
    }
}
