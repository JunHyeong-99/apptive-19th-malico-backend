package com.apptive.marico.service;

import com.apptive.marico.dto.RecommendStylistDto;
import com.apptive.marico.entity.Member;
import com.apptive.marico.entity.Stylist;
import com.apptive.marico.exception.CustomException;
import com.apptive.marico.exception.ErrorCode;
import com.apptive.marico.repository.MemberRepository;
import com.apptive.marico.repository.StylistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.apptive.marico.exception.ErrorCode.USER_NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberHomeService {
    private final MemberRepository memberRepository;
    private final StylistRepository stylistRepository;
    public List<RecommendStylistDto> recommendStylist(String userId) {
        Member member = memberRepository.findByUserId(userId).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND));

        // 추천 기준: 선호 스타일 > 지역(근거리 순)

        // 선호 스타일을 가지고 있는 스타일리스트 찾기
        List<Stylist> preferredStylists = stylistRepository.findByStylesIn(member.getPreferredStyles());

        preferredStylists = preferredStylists.stream()
                .filter(stylist -> {
                    // stylist와 member의 city와 state 값 중 null이 있는 경우 필터링에서 제외
                    String stylistCity = Optional.ofNullable(stylist.getCity()).orElse("");
                    String stylistState = Optional.ofNullable(stylist.getState()).orElse("");
                    String memberCity = Optional.ofNullable(member.getCity()).orElse("");
                    String memberState = Optional.ofNullable(member.getState()).orElse("");

                    // city와 state가 모두 일치하는 경우에만 필터링
                    return stylistCity.equals(memberCity) && stylistState.equals(memberState);
                })
                .sorted(Comparator.comparing(Stylist::getCity).thenComparing(Stylist::getState))
                .collect(Collectors.toList());

//        // 선호 스타일과 지역을 모두 만족하는 스타일리스트가 10명 미만일 경우, 선호 스타일만 고려하여 스타일리스트 찾기
//        if(preferredStylists.size() < 10) {
//            preferredStylists.addAll(stylistRepository.findByStylesInAndCityNotOrStateNot(member.getPreferredStyles(), member.getCity(), member.getState()));
//        }

        // 추천 스타일리스트가 10명이 넘을 경우, 리스트 크기 조정
        if(preferredStylists.size() > 10) {
            preferredStylists = preferredStylists.subList(0, 10);
        }

        // 추천 스타일리스트 정보를 RecommendStylistDto에 담아 반환
        List<RecommendStylistDto> recommendStylistDtos = preferredStylists.stream()
                .map(stylist -> new RecommendStylistDto(stylist.getId(), stylist.getProfileImage(), stylist.getOneLineIntroduction(), stylist.getStageName()))
                .collect(Collectors.toList());

        return recommendStylistDtos;
    }
}
