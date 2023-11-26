package com.apptive.marico.controller;

import com.apptive.marico.dto.findPwd.NewPwdRequestDto;
import com.apptive.marico.dto.member.MemberInformationDto;
import com.apptive.marico.dto.member.MemberMypageDto;
import com.apptive.marico.dto.mypage.LikedStylistListDto;
import com.apptive.marico.entity.Member;
import com.apptive.marico.entity.Stylist;
import com.apptive.marico.exception.CustomException;
import com.apptive.marico.service.MemberMypageService;
import com.apptive.marico.utils.ApiUtils;
import com.apptive.marico.utils.ApiUtils.ApiSuccess;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

import static com.apptive.marico.exception.ErrorCode.USER_NOT_FOUND;

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
    public ResponseEntity<MemberInformationDto> findInformation(Principal principal) {
        return ResponseEntity.ok(memberMyPageService.findInformation(principal.getName()));
    }

    // 현재 비밀번호가 일치한지 검사
    @GetMapping("/password")
    public ResponseEntity<ApiSuccess<Object>> checkCurrentPassword(Principal principal, @RequestParam String currentPassword) {
        return ResponseEntity.ok(new ApiSuccess<>(memberMyPageService.CheckCurrentPassword(principal.getName(), currentPassword)));
    }
    // 비밀번호 변경
    @PatchMapping("/password")
    public ResponseEntity<?> changePassword(Principal principal, @RequestParam String newPassword) {
        return ResponseEntity.ok(new ApiUtils.ApiSuccess<>(memberMyPageService.changePassword(principal.getName(), newPassword)));

    }

}
