package com.apptive.marico.service;


import com.apptive.marico.dto.finId.UserFindIdResponseDto;
import com.apptive.marico.entity.Member;
import com.apptive.marico.entity.Stylist;
import com.apptive.marico.entity.token.VerificationToken;
import com.apptive.marico.repository.MemberRepository;
import com.apptive.marico.repository.StylistRepository;
import com.apptive.marico.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

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

        String verificationCode = UUID.randomUUID().toString();
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
            throw new Exception("이메일을 다시 확인해 주세요");
        }
        return ;
    }

    // 인증 코드가 유효하면 userId를 리턴
    public UserFindIdResponseDto verifyUserEmailForId(String token)  {
        VerificationToken verificationToken = verificationTokenRepository.findByVerificationCode(token);
        if(verificationToken == null) {
            return new UserFindIdResponseDto("인증 코드가 일치하지 않습니다.");
        }
        //3분이 지난 경우
        if(!verificationToken.getExpiryDate().isAfter(LocalDateTime.now())) {
            verificationTokenRepository.delete(verificationToken);
            return new UserFindIdResponseDto("인증 시간이 초과 되었습니다.");
        }

        String UserId = verificationToken.getUserId();
        verificationTokenRepository.delete(verificationToken);
        if (UserId == null) {
            return new UserFindIdResponseDto("등록된 회원정보가 없습니다.");
        }
        return new UserFindIdResponseDto("회원님의 아이디는 " + UserId + "입니다.");
    }

    public boolean verifyUserEmailForPwd(String token)  {
        VerificationToken verificationToken = verificationTokenRepository.findByVerificationCode(token);

        //not null & 3분 안 지났을 때
        if ((verificationToken != null) && verificationToken.getExpiryDate().isAfter(LocalDateTime.now())) {
            String UserId = verificationToken.getUserId();
            if (UserId != null) {
                return true;
            }
        }
        return false;
    }




    private LocalDateTime calculateExpiryDate() {
        return LocalDateTime.now().plusMinutes(3);
    }

}
