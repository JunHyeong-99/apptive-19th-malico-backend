package com.apptive.marico.dto;


import com.apptive.marico.entity.Notice;
import lombok.Builder;
import lombok.Getter;


import java.time.LocalDateTime;

@Builder
@Getter
public class NoticeDto {

    private Long id;

    private String title;
    private String content;

    private LocalDateTime createDate;

    public static NoticeDto toDto(Notice notice) {
        return NoticeDto.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .createDate(notice.getCreateDate())
                .build();
    }
}
