package com.apptive.marico.dto.stylist.service;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;


@Builder
@Getter
@AllArgsConstructor
public class StylistServiceResponseDto {

    private List<StylistServiceDto> serviceList = new ArrayList<>();
}
