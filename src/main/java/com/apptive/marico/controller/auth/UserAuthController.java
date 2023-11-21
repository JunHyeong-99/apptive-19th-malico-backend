package com.apptive.marico.controller.auth;


import com.apptive.marico.dto.LoginDto;
import com.apptive.marico.dto.token.TokenRequestDto;
import com.apptive.marico.dto.token.TokenResponseDto;
import com.apptive.marico.dto.findPwd.ChangePwdResponseDto;
import com.apptive.marico.dto.findPwd.NewPwdRequestDto;
import com.apptive.marico.dto.findId.SendEmailRequestDto;
import com.apptive.marico.dto.verificationToken.SendEmailResponseDto;
import com.apptive.marico.dto.verificationToken.VerificationTokenResponseDto;
import com.apptive.marico.entity.Member;
import com.apptive.marico.entity.Stylist;
import com.apptive.marico.exception.CustomException;
import com.apptive.marico.repository.MemberRepository;
import com.apptive.marico.repository.StylistRepository;
import com.apptive.marico.service.MemberAuthService;
import com.apptive.marico.service.StylistAuthService;
import com.apptive.marico.service.UserAuthService;
import com.apptive.marico.service.VerificationTokenService;
import com.apptive.marico.utils.ApiUtils;
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
    private final UserAuthService userAuthService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody LoginDto loginDto) {
        return ResponseEntity.ok(userAuthService.login(loginDto));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody TokenRequestDto requestDto) {
        userAuthService.logout(requestDto);
        return ResponseEntity.ok().build();
    }

    //TODO: 회원가입시 이메일 인증을 받는 로직이라면 추가적인 로직의 변형이 필요합니다.
    @PostMapping("/sign/verification-code")
    public ResponseEntity<SendEmailResponseDto> sendEmailForSign(@RequestBody SendEmailRequestDto sendEmailRequestDto) {
        String email = sendEmailRequestDto.getEmail();
        return ResponseEntity.ok(new SendEmailResponseDto(verificationTokenService.createVerificationTokenForSign(email)));
    }
    @GetMapping("/sign/verification-code")
    public ResponseEntity<VerificationTokenResponseDto> checkCodeForSign(@RequestParam String code) {
        return ResponseEntity.ok(new VerificationTokenResponseDto(verificationTokenService.verifyUserEmailForSign(code)));
    }

    @PostMapping("/search/verification-code")
    public ResponseEntity<?> sendEmailForFind(@RequestBody SendEmailRequestDto sendEmailRequestDto) {
        String email = sendEmailRequestDto.getEmail();
        verificationTokenService.createVerificationToken(email);
        return ResponseEntity.ok(new ApiUtils.ApiSuccess<>("인증 번호가 전송 되었습니다."));
    }
//    @GetMapping("/search/verification-code") //구현의 이유가 무엇인가요?
//    public ResponseEntity<VerificationTokenResponseDto> checkCodeForIdOrPwd(@RequestParam String code) {
//        return ResponseEntity.ok(new VerificationTokenResponseDto(verificationTokenService.verifyUserEmailForIdOrPwd(code)));
//    }
    @GetMapping("/search/id")
    public ResponseEntity<?> returnId(@RequestParam String code) {
        return ResponseEntity.ok(ApiUtils.success(verificationTokenService.returnUserEmail(code)));
    }

    @PatchMapping("/search/password") //Restful 한 Url 설계가 필요해보입니다!
    public ResponseEntity<?> checkCodeForPwd(@RequestBody NewPwdRequestDto newPwdRequestDto) throws Exception {
        Optional<Stylist> stylist = stylistRepository.findByUserId(newPwdRequestDto.getUserId());
        Optional<Member> member = memberRepository.findByUserId(newPwdRequestDto.getUserId());
        if(stylist.isPresent()) {
            return ResponseEntity.ok(ApiUtils.success(stylistAuthService.changePassword(stylist.get() ,newPwdRequestDto)));
        }
        else if (member.isPresent()) {
            return ResponseEntity.ok(new ChangePwdResponseDto(memberAuthService.changePassword(member.get(), newPwdRequestDto.getPassword(), newPwdRequestDto.getCode())));
        }
        else {
            throw new CustomException(USER_NOT_FOUND);
        }
    }
}
