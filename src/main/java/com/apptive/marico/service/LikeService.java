package com.apptive.marico.service;

import com.apptive.marico.dto.LikeRequestDto;
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

import static com.apptive.marico.exception.ErrorCode.STYLIST_NOT_FOUND;
import static com.apptive.marico.exception.ErrorCode.USER_NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LikeService {
    private final MemberRepository memberRepository;
    private final StylistRepository stylistRepository;
    private final LikeRepository likeRepository;
    @Transactional
    public void addlike(String userId, LikeRequestDto likeRequestDto) {
        Member member = memberRepository.findByUserId(userId).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND));
        Stylist stylist = stylistRepository.findByUserId(likeRequestDto.getStylist_id()).orElseThrow(
                () -> new CustomException(STYLIST_NOT_FOUND));
        Like like = likeRepository.save(Like.builder()
                .member(member)
                .stylist(stylist).build());
        return;

    }
}
