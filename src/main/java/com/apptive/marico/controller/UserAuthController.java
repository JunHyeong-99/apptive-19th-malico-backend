package com.apptive.marico.controller;


<<<<<<< Updated upstream:src/main/java/com/apptive/marico/controller/UserAuthController.java
import com.apptive.marico.dto.findId.UserFindIdResponseDto;
import com.apptive.marico.dto.findPwd.ChangePwdResponseDto;
import com.apptive.marico.dto.findPwd.NewPwdRequestDto;
import com.apptive.marico.dto.findId.SendEmailRequestDto;
=======
import com.apptive.marico.dto.LoginDto;
import com.apptive.marico.dto.findId.SendEmailRequestDto;
import com.apptive.marico.dto.findId.UserFindIdResponseDto;
import com.apptive.marico.dto.findPwd.ChangePwdResponseDto;
import com.apptive.marico.dto.findPwd.NewPwdRequestDto;
import com.apptive.marico.dto.token.TokenRequestDto;
import com.apptive.marico.dto.token.TokenResponseDto;
>>>>>>> Stashed changes:src/main/java/com/apptive/marico/controller/auth/UserAuthController.java
import com.apptive.marico.dto.verificationToken.SendEmailResponseDto;
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


    @PostMapping("/sign/verification-code")
    public ResponseEntity<SendEmailResponseDto> sendEmailForSign(@RequestBody SendEmailRequestDto sendEmailRequestDto) {
        String email = sendEmailRequestDto.getEmail();
        return ResponseEntity.ok(new SendEmailResponseDto(verificationTokenService.createVerificationTokenForSign(email)));
    }
    @GetMapping("/sign/verification-code")
    public ResponseEntity<VerificationTokenResponseDto> checkCodeForSign(@RequestParam String code) {
        return ResponseEntity.ok(new VerificationTokenResponseDto(verificationTokenService.verifyUserEmailForSign(code)));
    }
<<<<<<< Updated upstream:src/main/java/com/apptive/marico/controller/UserAuthController.java
=======

    @PostMapping("/sign/verification-code")
    public ResponseEntity<SendEmailResponseDto> sendEmailForSign(@RequestBody SendEmailRequestDto sendEmailRequestDto) {
        String email = sendEmailRequestDto.getEmail();
        return ResponseEntity.ok(new SendEmailResponseDto(verificationTokenService.createVerificationTokenForSign(email)));
    }
    @GetMapping("/sign/verification-code")
    public ResponseEntity<VerificationTokenResponseDto> checkCodeForSign(@RequestParam String code) {
        return ResponseEntity.ok(new VerificationTokenResponseDto(verificationTokenService.verifyUserEmailForSign(code)));
    }
>>>>>>> Stashed changes:src/main/java/com/apptive/marico/controller/auth/UserAuthController.java
    @PostMapping("/search/verification-code")
    public ResponseEntity<SendEmailResponseDto> sendEmailForFind(@RequestBody SendEmailRequestDto sendEmailRequestDto) {
        String email = sendEmailRequestDto.getEmail();
        try {
            verificationTokenService.createVerificationToken(email);
            return ResponseEntity.ok(new SendEmailResponseDto("인증 번호가 전송 되었습니다."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new SendEmailResponseDto( "오류가 발생했습니다: " + e.getMessage()));
        }
    }
    @GetMapping("/search/verification-code")
    public ResponseEntity<VerificationTokenResponseDto> checkCodeForIdOrPwd(@RequestParam String code) {
        return ResponseEntity.ok(new VerificationTokenResponseDto(verificationTokenService.verifyUserEmailForIdOrPwd(code)));
    }

    @GetMapping("/search/id")
    public ResponseEntity<UserFindIdResponseDto> returnId(@RequestParam String code) {
        return ResponseEntity.ok(verificationTokenService.returnUserId(code));
    }

    @PatchMapping("/search/password")
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
