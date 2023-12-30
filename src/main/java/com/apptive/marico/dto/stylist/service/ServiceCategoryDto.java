package com.apptive.marico.dto.stylist.service;

import com.apptive.marico.dto.stylist.StylistMypageDto;
import com.apptive.marico.entity.*;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ServiceCategoryDto {

    @Enumerated(EnumType.STRING)
    private ServiceType serviceType;

    @Enumerated(EnumType.STRING)
    private ConnectionType connectionType;

    private String categoryDescription;

    public static ServiceCategoryDto toDto(ServiceCategory serviceCategory) {
        return ServiceCategoryDto.builder()
                .serviceType(serviceCategory.getServiceType())
                .connectionType(serviceCategory.getConnectionType())
                .categoryDescription(serviceCategory.getCategoryDescription())
                .build();
    }
}
