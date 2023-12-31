package com.apptive.marico.dto.stylist.service;



import com.apptive.marico.entity.StylistService;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
public class StylistServiceDto {
    private Long service_id;
    private String serviceName;
    private String serviceDescription;

    private List<ServiceCategoryDto> serviceCategoryDtoList = new ArrayList<>();
    private int price;

    public static StylistServiceDto toDto(StylistService stylistService) {
        List<ServiceCategoryDto> categoryDtoList = stylistService.getServiceCategory().stream()
                .map(ServiceCategoryDto::toDto)
                .toList();

        return StylistServiceDto.builder()
                .service_id(stylistService.getId())
                .serviceName(stylistService.getServiceName())
                .serviceDescription(stylistService.getServiceDescription())
                .serviceCategoryDtoList(categoryDtoList)
                .price(stylistService.getPrice())
                .build();
    }
}
