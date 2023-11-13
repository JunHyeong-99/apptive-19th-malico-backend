package com.apptive.marico.service;


import com.apptive.marico.dto.findId.UserFindIdResponseDto;
import com.apptive.marico.entity.Member;
import com.apptive.marico.entity.Stylist;
import com.apptive.marico.entity.token.VerificationToken;
import com.apptive.marico.exception.CustomException;
import com.apptive.marico.repository.MemberRepository;
import com.apptive.marico.repository.StylistRepository;
import com.apptive.marico.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static com.apptive.marico.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class VerificationTokenService {
    private final VerificationTokenRepository verificationTokenRepository;
    private final SmtpEmailService smtpEmailService;
    private final MemberRepository memberRepository;
    private final StylistRepository stylistRepository;

    public void createVerificationToken(String email) throws Exception{
        Optional<Member> findMember = memberRepository.findByEmail(email);
        Optional<Stylist> findStylist = stylistRepository.findByEmail(email);

        String verificationCode = generateVerificationCode();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setVerificationCode(verificationCode);

        if (findMember.isPresent()) {
            Member member = findMember.get();
            verificationToken.setUserId(member.getUserId());
            verificationToken.setExpiryDate(calculateExpiryDate());
            verificationTokenRepository.save(verificationToken);
            smtpEmailService.sendVerificationCode(member.getEmail(), verificationCode);
        }

        else if(findStylist.isPresent()) {
            Stylist stylist = findStylist.get();
            verificationToken.setUserId(stylist.getUserId());
            verificationToken.setExpiryDate(calculateExpiryDate());
            verificationTokenRepository.save(verificationToken);
            smtpEmailService.sendVerificationCode(stylist.getEmail(), verificationCode);
        }
        else {
            throw new CustomException(USER_NOT_FOUND);
        }
    }

    public String createVerificationTokenForSign(String email) {

        Optional<Member> findMember = memberRepository.findByEmail(email);
        Optional<Stylist> findStylist = stylistRepository.findByEmail(email);

        if (findMember.isPresent() || findStylist.isPresent()) {
            throw new CustomException(ALREADY_SAVED_EMAIL);
        }
        String verificationCode = generateVerificationCode();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setVerificationCode(verificationCode);

        verificationToken.setExpiryDate(calculateExpiryDate());
        verificationTokenRepository.save(verificationToken);
        smtpEmailService.sendVerificationCode(email, verificationCode);
        return "인증 번호가 전송 되었습니다.";
    }

    // 인증 코드가 유효하면 userId를 리턴
    public UserFindIdResponseDto returnUserId(String token)  {
        VerificationToken verificationToken = verificationTokenRepository.findByVerificationCode(token);
        if(checkToken(verificationToken)) {
            String UserId = verificationToken.getUserId();
            verificationTokenRepository.delete(verificationToken);
            if (UserId == null) {
                throw new CustomException(USER_NOT_FOUND);
            }
            return new UserFindIdResponseDto("회원님의 아이디는 " + UserId + "입니다.");
        }
        else {
            return new UserFindIdResponseDto("코드를 인증할 수 없습니다.");
        }

    }

    public boolean verifyUserEmailForIdOrPwd(String Code) {
        VerificationToken verificationToken = verificationTokenRepository.findByVerificationCode(Code);

        if (checkToken(verificationToken)) {
            verificationToken.setExpiryDate(LocalDateTime.now().plusMinutes(30));
            return true;
        }
        else return false;
    }

    public boolean verifyUserEmailForSign(String token)  {
        VerificationToken verificationToken = verificationTokenRepository.findByVerificationCode(token);

        if (checkToken(verificationToken)) {
            verificationTokenRepository.delete(verificationToken);
            return true;
        }
        return false;
    }


    private boolean checkToken(VerificationToken verificationToken) {
        if(verificationToken == null) {
            throw new CustomException(CODE_NOT_MATCH);
        }
        //시간이 지난 경우
        if(!verificationToken.getExpiryDate().isAfter(LocalDateTime.now())) {
            verificationTokenRepository.delete(verificationToken);
            throw new CustomException(VERIFICATION_CODE_TIMEOUT);
        }
        return true;
    }

    private String generateVerificationCode() {
        String verificationCode = UUID.randomUUID().toString();
        verificationCode = verificationCode.replaceAll("-", "");
        return verificationCode.substring(0, 6);
    }
    private LocalDateTime calculateExpiryDate() {
        return LocalDateTime.now().plusMinutes(3);
    }

}
