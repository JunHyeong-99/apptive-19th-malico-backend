package com.apptive.marico.controller;

import com.apptive.marico.dto.member.MemberMypageDto;
import com.apptive.marico.dto.mypage.MemberMypageEditDto;
import com.apptive.marico.dto.mypage.PasswordDto;
import com.apptive.marico.dto.mypage.LikedStylistListDto;
import com.apptive.marico.service.MemberMypageService;
import com.apptive.marico.utils.ApiUtils;
import com.apptive.marico.utils.ApiUtils.ApiSuccess;
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

    /**
     * 개인 정보 수정
     * @param principal
     * @return
     */
    // 조회
    @GetMapping("/information")
    public ResponseEntity<MemberMypageEditDto> findInformation(Principal principal) {
        return ResponseEntity.ok(memberMyPageService.getInformation(principal.getName()));
    }

    // 수정
    @PatchMapping("/information")
    public ResponseEntity<MemberMypageEditDto> updateInformation(Principal principal, @RequestBody MemberMypageEditDto memberMypageEditDto) {
        System.out.println(memberMypageEditDto.getBirthDate());

        return ResponseEntity.ok(memberMyPageService.updateInformation(principal.getName(), memberMypageEditDto));
    }


    // 현재 비밀번호가 일치한지 검사
    @GetMapping("/password")
    public ResponseEntity<ApiSuccess<Object>> checkCurrentPassword(Principal principal, @RequestBody PasswordDto passwordDto) {
        return ResponseEntity.ok(new ApiSuccess<>(memberMyPageService.CheckCurrentPassword(principal.getName(), passwordDto.getPassword())));
    }
    // 비밀번호 변경
    @PatchMapping("/password")
    public ResponseEntity<?> changePassword(Principal principal, @RequestBody PasswordDto passwordDto) {
        return ResponseEntity.ok(new ApiUtils.ApiSuccess<>(memberMyPageService.changePassword(principal.getName(), passwordDto.getPassword())));

    }

}
