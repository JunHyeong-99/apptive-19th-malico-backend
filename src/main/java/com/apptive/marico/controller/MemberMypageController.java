package com.apptive.marico.controller;

import com.apptive.marico.dto.member.MemberInformationDto;
import com.apptive.marico.dto.member.MemberMypageDto;
import com.apptive.marico.dto.mypage.LikedStylistListDto;
import com.apptive.marico.service.MemberMypageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/mypage/member")
@RequiredArgsConstructor
public class MemberMypageController {
    private final MemberMypageService memberMyPageService;
    // 마이페이지 조회
    @GetMapping("/")
    public ResponseEntity<MemberMypageDto> mypage(Principal principal) {
        return ResponseEntity.ok(memberMyPageService.mypage(principal.getName()));
    }
    // 프로필 사진 수정

    // 관심 스타일리스트
    @GetMapping("/liked-stylist")
    public ResponseEntity<LikedStylistListDto> findLikedStylist(Principal principal) {
        return ResponseEntity.ok(memberMyPageService.findLikedStylist(principal.getName()));
    }
}
