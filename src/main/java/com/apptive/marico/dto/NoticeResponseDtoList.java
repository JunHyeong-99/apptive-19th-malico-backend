package com.apptive.marico.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
public class NoticeResponseDtoList {
    List<NoticeDto> noticeDtoList = new ArrayList<>();
    List<Long> readNotice = new ArrayList<>();
}
