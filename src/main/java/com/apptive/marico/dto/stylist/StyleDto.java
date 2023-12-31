package com.apptive.marico.dto.stylist;

import com.apptive.marico.entity.Style;
import lombok.Builder;
import lombok.Getter;

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
}
