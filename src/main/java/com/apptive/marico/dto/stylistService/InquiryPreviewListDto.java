package com.apptive.marico.dto.stylistService;

import com.apptive.marico.dto.stylist.StyleDto;
import com.apptive.marico.entity.ServiceInquiry;
import com.apptive.marico.entity.Style;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
public class InquiryPreviewListDto {

    List<InquiryPreviewDto> inquiryPreviewDtoList;

    public static InquiryPreviewListDto toDto(List<ServiceInquiry> serviceInquiryList) {
        List<InquiryPreviewDto> inquiryPreviewDtoList = serviceInquiryList.stream()
                .map(InquiryPreviewDto::toDto)
                .collect(Collectors.toList());

        return InquiryPreviewListDto.builder()
                .inquiryPreviewDtoList(inquiryPreviewDtoList)
                .build();
    }
}
