package com.apptive.marico.dto.stylistService;


import com.apptive.marico.dto.stylist.StyleDto;
import com.apptive.marico.entity.ServiceInquiry;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class InquiryPreviewDto {
    private Long inquiryId;
    private String title;
    private String content;
    private boolean answerComplete;

    public static InquiryPreviewDto toDto(ServiceInquiry serviceInquiry) {

        return InquiryPreviewDto.builder()
                .inquiryId(serviceInquiry.getId())
                .title(serviceInquiry.getTitle())
                .content(serviceInquiry.getContent())
                .answerComplete(serviceInquiry.isAnswerComplete())
                .build();
    }
}
