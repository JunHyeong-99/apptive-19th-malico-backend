package com.apptive.marico.controller.auth;


import com.apptive.marico.dto.LoginDto;
import com.apptive.marico.dto.token.TokenRequestDto;
import com.apptive.marico.dto.token.TokenDto;
import com.apptive.marico.dto.findPwd.NewPwdRequestDto;
import com.apptive.marico.dto.findId.SendEmailRequestDto;
import com.apptive.marico.dto.token.TokenResponseDto;
import com.apptive.marico.dto.verificationToken.SendEmailResponseDto;
import com.apptive.marico.entity.Member;
import com.apptive.marico.entity.Stylist;
import com.apptive.marico.exception.CustomException;
import com.apptive.marico.repository.MemberRepository;
import com.apptive.marico.repository.StylistRepository;
import com.apptive.marico.service.*;
import com.apptive.marico.service.auth.CustomUserDetailsService;
import com.apptive.marico.service.auth.MemberAuthService;
import com.apptive.marico.service.auth.StylistAuthService;
import com.apptive.marico.utils.ApiUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.apptive.marico.exception.ErrorCode.USER_NOT_FOUND;

@RestController
@RequestMapping("/auth/user")
@RequiredArgsConstructor
public class UserAuthController {
    private final VerificationTokenService verificationTokenService;
    private final StylistRepository stylistRepository;
    private final MemberRepository memberRepository;
    private final StylistAuthService stylistAuthService;
    private final MemberAuthService memberAuthService;
    private final CustomUserDetailsService customUserDetailsService;

    @PostMapping("/login")
    public ResponseEntity<?> login(HttpServletResponse response, @RequestBody LoginDto loginDto) {
        TokenDto tokenDto = customUserDetailsService.login(loginDto);
        response.setHeader("Set-Cookie",
                "refreshToken=" + tokenDto.getRefreshToken() + "; Path=/; HttpOnly; SameSite=Strict; Secure; expires=" + tokenDto.getRefreshTokenExpiresIn());
        return ResponseEntity.ok(TokenResponseDto.builder()
                .grantType(tokenDto.getGrantType())
                .accessToken(tokenDto.getAccessToken())
                .accessTokenExpiresIn(tokenDto.getAccessTokenExpiresIn())
                .build());
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request,  HttpServletResponse response, @CookieValue String refreshToken) {
        System.out.println(refreshToken);
        String accessToken = request.getHeader("authorization").substring(7);
        TokenDto tokenDto = customUserDetailsService.refreshToken(accessToken, refreshToken);
        response.setHeader("Set-Cookie",
                "refreshToken=" + tokenDto.getRefreshToken() + "; Path=/; HttpOnly; SameSite=Strict; Secure;");
        return ResponseEntity.ok(TokenResponseDto.builder()
                .grantType(tokenDto.getGrantType())
                .accessToken(tokenDto.getAccessToken())
                .accessTokenExpiresIn(tokenDto.getAccessTokenExpiresIn())
                .build());
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody TokenRequestDto requestDto) {
        customUserDetailsService.logout(requestDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/sign/verification-code")
    public ResponseEntity<SendEmailResponseDto> sendEmailForSign(@RequestBody SendEmailRequestDto sendEmailRequestDto) {
        String email = sendEmailRequestDto.getEmail();
        return ResponseEntity.ok(new SendEmailResponseDto(verificationTokenService.createVerificationTokenForSign(email)));
    }
    @GetMapping("/sign/verification-code")
    public ResponseEntity<?> checkCodeForSign(@RequestParam String code) {
        return ResponseEntity.ok(new ApiUtils.ApiSuccess<>(verificationTokenService.verifyUserEmailForSign(code)));
    }

    @PostMapping("/search/verification-code") // 찾기 기능 인증번호 생성
    public ResponseEntity<?> sendEmailForFind(@RequestBody SendEmailRequestDto sendEmailRequestDto) {
        String email = sendEmailRequestDto.getEmail();
        verificationTokenService.createVerificationToken(email);
        return ResponseEntity.ok(new ApiUtils.ApiSuccess<>("인증 번호가 전송 되었습니다."));
    }

    @GetMapping("/search/verification-code") // 인증코드 체크 인증 코드 확인 성공하고 다음 페이지 넘어가는 시간 30분 추가
    public ResponseEntity<?> checkCodeForIdOrPwd(@RequestParam String code) {
        return ResponseEntity.ok(new ApiUtils.ApiSuccess<>(verificationTokenService.checkTokenAndSetExpiryDate(code)));
    }
    @GetMapping("/search/id") // 인증 코드 체크 후 아이디 리턴
    public ResponseEntity<?> returnId(@RequestParam String code) {
        return ResponseEntity.ok(ApiUtils.success(verificationTokenService.returnUserId(code)));
    }

    @PatchMapping("/search/password") // 비밀번호 변경
    public ResponseEntity<?> changePwd(@RequestBody NewPwdRequestDto newPwdRequestDto) throws Exception {
        Optional<Stylist> stylist = stylistRepository.findByUserId(newPwdRequestDto.getUserId());
        Optional<Member> member = memberRepository.findByUserId(newPwdRequestDto.getUserId());
        if(stylist.isPresent()) {
            return ResponseEntity.ok(new ApiUtils.ApiSuccess<>(stylistAuthService.changePassword(stylist.get() ,newPwdRequestDto)));
        }
        else if (member.isPresent()) {
            return ResponseEntity.ok(new ApiUtils.ApiSuccess<>(memberAuthService.changePassword(member.get(), newPwdRequestDto.getPassword(), newPwdRequestDto.getCode())));
        }
        else {
            throw new CustomException(USER_NOT_FOUND);
        }
    }
}
