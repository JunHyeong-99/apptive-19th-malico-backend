package com.apptive.marico.dto.stylistService;


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
}
