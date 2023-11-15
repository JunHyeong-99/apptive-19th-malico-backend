package com.apptive.marico.service;

import com.apptive.marico.dto.member.MemberMypageDto;
import com.apptive.marico.entity.Member;
import com.apptive.marico.exception.CustomException;
import com.apptive.marico.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.apptive.marico.exception.ErrorCode.USER_NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberMypageService {
    private final MemberRepository memberRepository;
    @Transactional
    public MemberMypageDto mypage(String userId) {
        Member member = memberRepository.findByUserId(userId).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND));
        return MemberMypageDto.toDto(member);
    }


}
