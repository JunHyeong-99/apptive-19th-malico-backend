package com.apptive.marico.dto.stylistService;


import com.apptive.marico.dto.stylist.service.StylistServiceDto;
import com.apptive.marico.entity.ServiceInquiry;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
public class InquiryDto {

    private Long service_id;
    private String title;
    private String content;
    private List<String> img = new ArrayList<>();


    @Builder
    @Getter
    public static class InquiryResponseDto {
        private StylistServiceDto stylistServiceDto;
        private String title;
        private String content;
        private List<String> img = new ArrayList<>();
    }

    public static InquiryDto toDto(ServiceInquiry serviceInquiry) {
        return InquiryDto.builder()
                .service_id(serviceInquiry.getId())
                .title(serviceInquiry.getTitle())
                .content(serviceInquiry.getContent())
                .img(serviceInquiry.getInquiryImg())
                .build();
    }
}
