package com.apptive.marico.dto.stylistService;


import com.apptive.marico.dto.stylist.service.StylistServiceDto;
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
}
