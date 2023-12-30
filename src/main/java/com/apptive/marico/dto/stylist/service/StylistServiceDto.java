package com.apptive.marico.dto.stylist.service;


import com.apptive.marico.dto.stylist.StylistMypageDto;
import com.apptive.marico.entity.ConnectionType;
import com.apptive.marico.entity.ServiceType;
import com.apptive.marico.entity.Stylist;
import com.apptive.marico.entity.StylistService;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
public class StylistServiceDto {
    private String serviceName;
    private String serviceDescription;

    private List<ServiceCategoryDto> serviceCategoryDtoList = new ArrayList<>();
    private int price;

    public static StylistServiceDto toDto(StylistService stylistService) {
        List<ServiceCategoryDto> categoryDtoList = stylistService.getServiceCategory().stream()
                .map(ServiceCategoryDto::toDto)
                .toList();

        return StylistServiceDto.builder()
                .serviceName(stylistService.getServiceName())
                .serviceDescription(stylistService.getServiceDescription())
                .serviceCategoryDtoList(categoryDtoList)
                .price(stylistService.getPrice())
                .build();
    }
}
