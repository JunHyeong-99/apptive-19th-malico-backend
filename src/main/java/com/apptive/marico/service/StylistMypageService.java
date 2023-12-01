package com.apptive.marico.service;

import com.apptive.marico.dto.CareerDto;
import com.apptive.marico.dto.member.MemberMypageDto;
import com.apptive.marico.dto.stylist.StylistMypageDto;
import com.apptive.marico.dto.stylist.StylistMypageEditDto;
import com.apptive.marico.entity.Career;
import com.apptive.marico.entity.Member;
import com.apptive.marico.entity.Stylist;
import com.apptive.marico.exception.CustomException;
import com.apptive.marico.repository.CareerRepository;
import com.apptive.marico.repository.MemberRepository;
import com.apptive.marico.repository.StylistRepository;
import jakarta.persistence.Column;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.apptive.marico.exception.ErrorCode.USER_NOT_FOUND;

@Service
@Transactional
@RequiredArgsConstructor
public class StylistMypageService {
    private final StylistRepository stylistRepository;
    private final CareerRepository careerRepository;
    @Transactional
    public StylistMypageDto mypage(String userId) {
        Stylist stylist = stylistRepository.findByUserId(userId).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND));
        return StylistMypageDto.toDto(stylist);
    }

    public StylistMypageEditDto getInformation(String userId) {
        Stylist stylist = stylistRepository.findByUserId(userId).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND));

        return StylistMypageEditDto.toDto(stylist);
    }

    public String editInformation(String userId, StylistMypageEditDto stylistMypageEditDto) {
        Stylist stylist = stylistRepository.findByUserId(userId).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND));

        // 기존 career 삭제 후 다음 career 등록
        careerRepository.deleteByStylist_Id(stylist.getId());
        //연관관계 주인을 통한 새로운 career 저장
        List<CareerDto> careerDtoList = stylistMypageEditDto.getCareerDtoList();
        careerDtoList.stream()
                .map(careerDto -> createCareer(careerDto, stylist))
                .forEach(careerRepository::save);
        //다른 attribute 저장
        stylist.setProfileImage(stylistMypageEditDto.getProfile_image());
        stylist.setNickname(stylistMypageEditDto.getNickname());
        stylist.setOneLineIntroduction(stylistMypageEditDto.getOneLineIntroduction());
        stylist.setStylistIntroduction(stylistMypageEditDto.getStylistIntroduction());
        stylist.setCity(stylistMypageEditDto.getCity());
        stylist.setState(stylistMypageEditDto.getState());
        stylist.setChat_link(stylistMypageEditDto.getChat_link());
        return "정상적으로 입력되었습니다.";
    }

    private static Career createCareer(CareerDto careerDto, Stylist stylist) {
        return Career.builder()
                .organizationName(careerDto.getOrganizationName())
                .content(careerDto.getContent())
                .startYear(careerDto.getStartYear())
                .endYear(careerDto.getEndYear())
                .stylist(stylist)
                .build();
    }
}