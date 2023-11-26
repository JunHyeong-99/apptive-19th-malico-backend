package com.apptive.marico.service;

import com.apptive.marico.dto.member.MemberInformationDto;
import com.apptive.marico.dto.member.MemberMypageDto;
import com.apptive.marico.dto.mypage.LikedStylistDto;
import com.apptive.marico.dto.mypage.LikedStylistListDto;
import com.apptive.marico.entity.Like;
import com.apptive.marico.entity.Member;
import com.apptive.marico.entity.Stylist;
import com.apptive.marico.exception.CustomException;
import com.apptive.marico.repository.LikeRepository;
import com.apptive.marico.repository.MemberRepository;
import com.apptive.marico.repository.StylistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.apptive.marico.exception.ErrorCode.USER_NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberMypageService {
    private final MemberRepository memberRepository;
    private final StylistRepository stylistRepository;
    private final LikeRepository likeRepository;
    public MemberMypageDto mypage(String userId) {
        Member member = memberRepository.findByUserId(userId).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND));
        return MemberMypageDto.toDto(member);
    }


    public LikedStylistListDto findLikedStylist(String userId) {
        Member member = memberRepository.findByUserId(userId).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND));
        List<Like> likedStylists = likeRepository.findAllByMemberId(member.getId());
        List<LikedStylistDto> likedStylistsDto = new ArrayList<>();


        for (Like like : likedStylists) {
            Stylist stylist = stylistRepository.findById(like.getStylist().getId())
                    .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
            likedStylistsDto.add(LikedStylistDto.toDto(stylist));
        }

        return LikedStylistListDto.toDto(likedStylistsDto);
    }

    public MemberInformationDto findInformation(String userId) {
        // 닉네임, 성, 이메일
        Member member = memberRepository.findByUserId(userId).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND));
        return MemberInformationDto.toDto(member);
    }
}
