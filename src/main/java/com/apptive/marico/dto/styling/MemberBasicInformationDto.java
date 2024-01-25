package com.apptive.marico.dto.styling;

import com.apptive.marico.entity.Member;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberBasicInformationDto {
    private String height; // 필수
    private String weight; // 필수
    private List<Long> preferredStyles = new ArrayList<>(); // 필수, 최대 10개까지
    private String city; // 필수
    private String state; // 필수
    private String bodyShapeImage; // 필수
    private List<String> myStyles= new ArrayList<>();
    private List<String> referenceImages= new ArrayList<>();
    private String note; // 필수

    @Override
    public String toString() {
        return "MemberBasicInformationDto{" +
                "height='" + height + '\'' +
                ", weight='" + weight + '\'' +
                ", preferredStyles=" + preferredStyles +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", bodyShapeImage='" + bodyShapeImage + '\'' +
                ", myStyles=" + myStyles +
                ", referenceImages=" + referenceImages +
                ", note='" + note + '\'' +
                '}';
    }

    static public MemberBasicInformationDto toDto(Member member, List<Long> preferredStyleIds,
                                           List<String> myStyleUrls, List<String> referenceImageUrls) {
        return MemberBasicInformationDto.builder()
                .height(member.getHeight())
                .weight(member.getWeight())
                .preferredStyles(preferredStyleIds)
                .city(member.getCity().getDisplayName())
                .state(member.getState())
                .bodyShapeImage(member.getBodyShapeImages())
                .myStyles(myStyleUrls)
                .referenceImages(referenceImageUrls)
                .note(member.getNote())
                .build();
    }

}
