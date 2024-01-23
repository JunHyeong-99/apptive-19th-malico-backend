package com.apptive.marico.dto.styling;

import com.apptive.marico.entity.Stylist;
import lombok.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PersonalStylistDto {
    private String profileImage;
    private String stageName;

    static public PersonalStylistDto toDto(Stylist stylist) {
        return PersonalStylistDto.builder()
                .profileImage(stylist.getProfileImage())
                .stageName(stylist.getStageName())
                .build();
    }
}
