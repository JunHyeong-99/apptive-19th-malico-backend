package com.apptive.marico.controller.auth;

import com.apptive.marico.dto.LoginDto;
import com.apptive.marico.dto.member.MemberRequestDto;
import com.apptive.marico.dto.member.MemberResponseDto;
import com.apptive.marico.dto.token.TokenRequestDto;
import com.apptive.marico.dto.token.TokenResponseDto;
import com.apptive.marico.service.CustomUserDetailsService;
import com.apptive.marico.service.MemberAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/member")
@RequiredArgsConstructor
public class MemberAuthController {
    private final MemberAuthService memberAuthService;
    private final CustomUserDetailsService customUserDetailsService;


    @PostMapping("/signup")
    public ResponseEntity<MemberResponseDto> signup(@RequestBody MemberRequestDto memberRequestDto) {
        return ResponseEntity.ok(memberAuthService.signup(memberRequestDto));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody LoginDto loginDto) {
        return ResponseEntity.ok(customUserDetailsService.login(loginDto));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody TokenRequestDto requestDto) {
        customUserDetailsService.logout(requestDto);
        return ResponseEntity.ok().build();
    }
}
