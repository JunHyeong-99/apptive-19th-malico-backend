package com.apptive.marico.controller;

import com.apptive.marico.dto.member.LoginDto;
import com.apptive.marico.dto.member.MemberRequestDto;
import com.apptive.marico.dto.member.MemberResponseDto;
import com.apptive.marico.dto.token.TokenResponseDto;
import com.apptive.marico.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    @PostMapping("/signup")
    public ResponseEntity<MemberResponseDto> signup(@RequestBody MemberRequestDto memberRequestDto) {
        return ResponseEntity.ok(authService.signup(memberRequestDto));
    }
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody LoginDto loginDto) {
        return ResponseEntity.ok(authService.login(loginDto));
    }


}
