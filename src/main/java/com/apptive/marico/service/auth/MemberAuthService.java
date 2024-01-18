package com.apptive.marico.service.auth;

import com.apptive.marico.dto.mypage.member.MemberRequestDto;
import com.apptive.marico.dto.mypage.member.MemberResponseDto;
import com.apptive.marico.entity.Member;
import com.apptive.marico.entity.Role;
import com.apptive.marico.entity.token.VerificationToken;
import com.apptive.marico.exception.CustomException;
import com.apptive.marico.repository.MemberRepository;
import com.apptive.marico.repository.RoleRepository;
import com.apptive.marico.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;

import static com.apptive.marico.entity.Role.RoleName.ROLE_MEMBER;
import static com.apptive.marico.exception.ErrorCode.*;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberAuthService {
    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    private final VerificationTokenRepository verificationTokenRepository;

    private final CustomUserDetailsService customUserDetailsService;

    @Transactional
    public MemberResponseDto signup(MemberRequestDto memberRequestDto) {
        // 유효성 검사
        customUserDetailsService.checkAvailability(memberRequestDto);

        Role userRole = roleRepository.findByName(ROLE_MEMBER).orElseThrow(
                () -> new CustomException(ROLE_NOT_FOUND));


        Member member = memberRequestDto.toMember(passwordEncoder);
        member.setRoles(Collections.singleton(userRole));

        return MemberResponseDto.toDto(memberRepository.save(member));
    }



    public String changePassword(Member member, String password, String code) {
        VerificationToken verificationToken = verificationTokenRepository.findByVerificationCode(code);
        if (verificationToken == null) throw new CustomException(VERIFICATION_CODE_INVAILD);;
        if(!verificationToken.getExpiryDate().isAfter(LocalDateTime.now())) {
            verificationTokenRepository.delete(verificationToken);
            throw new CustomException(VERIFICATION_CODE_TIMEOUT);
        }
        if(member == null) throw new CustomException(MEMBER_NOT_FOUND);
        verificationTokenRepository.delete(verificationToken);
        member.setPassword(passwordEncoder.encode(password));
        memberRepository.save(member);
        return "비밀번호가 변경되었습니다.";
    }

}
