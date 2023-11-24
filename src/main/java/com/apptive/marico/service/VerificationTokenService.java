package com.apptive.marico.service;


import com.apptive.marico.entity.Member;
import com.apptive.marico.entity.Stylist;
import com.apptive.marico.entity.token.VerificationToken;
import com.apptive.marico.exception.CustomException;
import com.apptive.marico.repository.MemberRepository;
import com.apptive.marico.repository.StylistRepository;
import com.apptive.marico.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.apptive.marico.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class VerificationTokenService {
    private final VerificationTokenRepository verificationTokenRepository;
    private final SmtpEmailService smtpEmailService;
    private final MemberRepository memberRepository;
    private final StylistRepository stylistRepository;

    public void createVerificationToken(String email) {
        if (!isEmailRegistered(email)) {
            throw new CustomException(USER_NOT_FOUND);
        }
        VerificationToken token = VerificationToken.create(email);
        verificationTokenRepository.save(token);
        smtpEmailService.sendVerificationCode(email, token.getVerificationCode());
    }


    public String createVerificationTokenForSign(String email) {

        if (isEmailRegistered(email)) {
            throw new CustomException(ALREADY_SAVED_EMAIL);
        }

        VerificationToken token = VerificationToken.create(email);
        verificationTokenRepository.save(token);
        smtpEmailService.sendVerificationCode(email, token.getVerificationCode());
        return "인증 번호가 전송 되었습니다.";
    }

    private boolean isEmailRegistered(String email) {
        return memberRepository.existsByEmail(email) || stylistRepository.existsByEmail(email);
    }

    // 인증 코드가 유효하면 userEmail를 리턴
    public String returnUserId(String token)  {
        VerificationToken verificationToken = verificationTokenRepository.findByVerificationCode(token);
        if (checkToken(verificationToken)) {
            Optional<Member> member = memberRepository.findByEmail(verificationToken.getEmail());
            Optional<Stylist> stylist = stylistRepository.findByEmail(verificationToken.getEmail());
            if (member.isPresent()) {
                return member.get().getUserId();
            }
            else if (stylist.isPresent()) {
                return stylist.get().getUserId();
            }
            else throw new CustomException(USER_NOT_FOUND);
        }
        throw new CustomException(VERIFICATION_CODE_INVAILD);
    }

    public String checkTokenAndSetExpiryDate(String Code) {
        VerificationToken verificationToken = verificationTokenRepository.findByVerificationCode(Code);

        if (checkToken(verificationToken)) {
            verificationToken.setExpiryDate(LocalDateTime.now().plusMinutes(30));
            verificationTokenRepository.save(verificationToken);
            return "이메일 인증에 성공했습니다 ";
        }
        else return "발급 코드가 유효하지 않습니다.";
    }

    public String verifyUserEmailForSign(String token)  {
        VerificationToken verificationToken = verificationTokenRepository.findByVerificationCode(token);
        if (checkToken(verificationToken)) {
            return "이메일 인증에 성공했습니다.";
        }
        else {
            return "이메일 인증에 실패했습니다.";
        }

    }


    public boolean checkToken(VerificationToken verificationToken) {
        if(verificationToken == null) {
            throw new CustomException(VERIFICATION_CODE_INVAILD);
        }
        //시간이 지난 경우
        if(!verificationToken.getExpiryDate().isAfter(LocalDateTime.now())) {
            verificationTokenRepository.delete(verificationToken);
            throw new CustomException(VERIFICATION_CODE_TIMEOUT);
        }
        String email = verificationToken.getEmail();
        if (email == null) {
            throw new CustomException(USER_NOT_FOUND);
        }
        verificationTokenRepository.delete(verificationToken);

        return true;
    }

}
