package com.apptive.marico.dto.styling;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MyClientDto {
    List<MyClientMemberDto> myClients;

    static public MyClientDto toDto(List<MyClientMemberDto> myClients) {
        return MyClientDto.builder()
                .myClients(myClients)
                .build();
    }
}
