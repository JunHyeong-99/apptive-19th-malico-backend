package com.apptive.marico.service;

import com.apptive.marico.dto.member.MemberMypageDto;
import com.apptive.marico.dto.mypage.LikedStylistDto;
import com.apptive.marico.dto.mypage.LikedStylistListDto;
import com.apptive.marico.dto.mypage.MemberMypageEditDto;
import com.apptive.marico.entity.Like;
import com.apptive.marico.entity.Member;
import com.apptive.marico.entity.Stylist;
import com.apptive.marico.exception.CustomException;
import com.apptive.marico.repository.LikeRepository;
import com.apptive.marico.repository.MemberRepository;
import com.apptive.marico.repository.StylistRepository;
import com.apptive.marico.service.auth.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.apptive.marico.exception.ErrorCode.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberMypageService {
    private final MemberRepository memberRepository;
    private final StylistRepository stylistRepository;
    private final LikeRepository likeRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService customUserDetailsService;

    private final Logger log = LoggerFactory.getLogger(getClass());
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

    public MemberMypageEditDto getInformation(String userId) {
        // 닉네임, 성, 이메일
        Member member = memberRepository.findByUserId(userId).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND));
        return MemberMypageEditDto.toDto(member);
    }

    @Transactional
    public MemberMypageEditDto updateInformation(String userId, MemberMypageEditDto memberMypageEditDto) {
        Member originalMember = memberRepository.findByUserId(userId).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND));

        Member updateMember = originalMember;

        if (memberMypageEditDto.getNickname() != null)
            updateMember.setNickname(memberMypageEditDto.getNickname());
        if (memberMypageEditDto.getGender() != 0) updateMember.setGender(memberMypageEditDto.getGender());
        if (memberMypageEditDto.getBirthDate() != null)
            updateMember.setBirthDate(memberMypageEditDto.getBirthDate());

        Member member = memberRepository.save(updateMember);
        return MemberMypageEditDto.toDto(member);
    }

    public String CheckCurrentPassword(String userId, String password) {
        Member member = memberRepository.findByUserId(userId).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND));

        // matches 메서드는 원본 패스워드와 인코딩된 패스워드를 비교하여 일치 여부를 확인
        if(!passwordEncoder.matches(password, member.getPassword())){
            throw new CustomException(PASSWORD_NOT_MATCH);
        }
        return "비밀번호가 일치합니다.";
    }

    @Transactional
    public String changePassword(String userId, String newPassword) {
        Member member = memberRepository.findByUserId(userId).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND));

        customUserDetailsService.checkPasswordAvailability(newPassword);

        member.setPassword(passwordEncoder.encode(newPassword));
        memberRepository.save(member);

        return "비밀번호가 변경되었습니다.";
    }
}
