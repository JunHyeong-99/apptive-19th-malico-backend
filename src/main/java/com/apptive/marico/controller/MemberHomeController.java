package com.apptive.marico.controller;

import com.apptive.marico.dto.mypage.member.MemberMypageDto;
import com.apptive.marico.service.MemberHomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/home/member")
@RequiredArgsConstructor
public class MemberHomeController {
    private final MemberHomeService memberHomeService;
    @GetMapping("/recommend")
    public ResponseEntity<?> recommendStylist(Principal principal) {
        return ResponseEntity.ok(memberHomeService.recommendStylist(principal.getName()));
    }

}
