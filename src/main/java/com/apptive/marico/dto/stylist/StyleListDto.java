package com.apptive.marico.dto.stylist;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class StyleListDto {
    private List<StyleDto> styleDtoList = new ArrayList<>();
}
