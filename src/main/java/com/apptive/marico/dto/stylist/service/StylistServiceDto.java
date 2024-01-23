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

    private ServiceCategoryDto serviceCategoryDto;
    private int price;

    public static StylistServiceDto toDto(StylistService stylistService) {

        ServiceCategoryDto categoryDto = ServiceCategoryDto.toDto(stylistService.getServiceCategory());
        return StylistServiceDto.builder()
                .service_id(stylistService.getId())
                .serviceName(stylistService.getServiceName())
                .serviceDescription(stylistService.getServiceDescription())
                .serviceCategoryDto(categoryDto)
                .price(stylistService.getPrice())
                .build();
    }
}
