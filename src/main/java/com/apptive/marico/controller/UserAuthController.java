package com.apptive.marico.controller;


import com.apptive.marico.dto.findId.UserFindIdResponseDto;
import com.apptive.marico.dto.findPwd.ChangePwdResponseDto;
import com.apptive.marico.dto.findPwd.NewPwdRequestDto;
import com.apptive.marico.dto.findId.SendEmailRequestDto;
import com.apptive.marico.dto.verificationToken.SendEmailResponseDto;
import com.apptive.marico.dto.verificationToken.VerificationTokenRequestDto;
import com.apptive.marico.dto.verificationToken.VerificationTokenResponseDto;
import com.apptive.marico.entity.Member;
import com.apptive.marico.entity.Stylist;
import com.apptive.marico.exception.CustomException;
import com.apptive.marico.repository.MemberRepository;
import com.apptive.marico.repository.StylistRepository;
import com.apptive.marico.service.MemberAuthService;
import com.apptive.marico.service.StylistAuthService;
import com.apptive.marico.service.VerificationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    @PostMapping("/find/send-email")
    public ResponseEntity<SendEmailResponseDto> sendEmailForFind(@RequestBody SendEmailRequestDto sendEmailRequestDto) {
        String email = sendEmailRequestDto.getEmail();
        try {
            verificationTokenService.createVerificationToken(email);
            return ResponseEntity.ok(new SendEmailResponseDto("인증 번호가 전송 되었습니다."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new SendEmailResponseDto( "오류가 발생했습니다: " + e.getMessage()));
        }
    }

    @PostMapping("/sign/send-email")
    public ResponseEntity<SendEmailResponseDto> sendEmailForSign(@RequestBody SendEmailRequestDto sendEmailRequestDto) {
        String email = sendEmailRequestDto.getEmail();

        try {
            return ResponseEntity.ok(new SendEmailResponseDto(verificationTokenService.createVerificationTokenForSign(email)));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new SendEmailResponseDto("오류가 발생했습니다: " + e.getMessage()));
        }
    }
    @PostMapping("/find-id/check-code")
    public ResponseEntity<VerificationTokenResponseDto> checkCodeForId(@RequestBody VerificationTokenRequestDto verificationTokenRequestDto) {
        return ResponseEntity.ok(new VerificationTokenResponseDto(verificationTokenService.verifyUserEmailForIdOrPwd(verificationTokenRequestDto.getCode())));
    }

    @PostMapping("/check-id")
    public ResponseEntity<UserFindIdResponseDto> checkId(@RequestBody VerificationTokenRequestDto verificationTokenRequestDto) {
        return ResponseEntity.ok(verificationTokenService.returnUserId(verificationTokenRequestDto.getCode()));
    }
    @PostMapping("/sign/check-code")
    public ResponseEntity<VerificationTokenResponseDto> checkCodeForSign(@RequestBody VerificationTokenRequestDto verificationTokenRequestDto) {
        return ResponseEntity.ok(new VerificationTokenResponseDto(verificationTokenService.verifyUserEmailForSign(verificationTokenRequestDto.getCode())));
    }
    @PostMapping("/change-pwd/check-code")
    public ResponseEntity<VerificationTokenResponseDto> checkCodeForPwd(@RequestBody VerificationTokenRequestDto verificationTokenRequestDto) {
        return ResponseEntity.ok(new VerificationTokenResponseDto(verificationTokenService.verifyUserEmailForIdOrPwd(verificationTokenRequestDto.getCode())));
    }

    @PostMapping("/change-pwd")
    public ResponseEntity<ChangePwdResponseDto> checkCodeForPwd(@RequestBody NewPwdRequestDto newPwdRequestDto) throws Exception {
        Optional<Stylist> findStylist = stylistRepository.findByUserId(newPwdRequestDto.getUserId());
        Optional<Member> findMember = memberRepository.findByUserId(newPwdRequestDto.getUserId());
        if(findStylist.isPresent()) {
            return ResponseEntity.ok(new ChangePwdResponseDto(stylistAuthService.changePassword(findStylist.get(), newPwdRequestDto.getPassword(), newPwdRequestDto.getCode())));
        }

        else if (findMember.isPresent()) {
            return ResponseEntity.ok(new ChangePwdResponseDto(memberAuthService.changePassword(findMember.get(), newPwdRequestDto.getPassword(), newPwdRequestDto.getCode())));
        }
        else {
            throw new CustomException(USER_NOT_FOUND);
        }
    }
}
