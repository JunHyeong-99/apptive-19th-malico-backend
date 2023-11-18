package com.apptive.marico.service;

import com.apptive.marico.dto.LoginDto;
import com.apptive.marico.dto.member.MemberRequestDto;
import com.apptive.marico.dto.member.MemberResponseDto;
import com.apptive.marico.dto.token.TokenRequestDto;
import com.apptive.marico.dto.token.TokenResponseDto;
import com.apptive.marico.entity.Member;
import com.apptive.marico.entity.Role;
import com.apptive.marico.entity.Stylist;
import com.apptive.marico.entity.token.RefreshToken;
import com.apptive.marico.entity.token.VerificationToken;
import com.apptive.marico.exception.CustomException;
import com.apptive.marico.jwt.TokenProvider;
import com.apptive.marico.repository.MemberRepository;
import com.apptive.marico.repository.RefreshTokenRepository;
import com.apptive.marico.repository.RoleRepository;
import com.apptive.marico.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
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

        customUserDetailsService.checkEmailAvailability(memberRequestDto.getEmail());

        Role userRole = roleRepository.findByName(ROLE_MEMBER).orElseThrow(
                () -> new CustomException(ROLE_NOT_FOUND));

        Member member = memberRequestDto.toMember(passwordEncoder);
        member.setRoles(Collections.singleton(userRole));

        return MemberResponseDto.toDto(memberRepository.save(member));
    }

    public String changePassword(Member member, String password, String code) throws Exception{
        VerificationToken verificationToken = verificationTokenRepository.findByVerificationCode(code);
        if (verificationToken == null) return "인증번호가 일치하지 않습니다.";
        if(!verificationToken.getExpiryDate().isAfter(LocalDateTime.now())) {
            verificationTokenRepository.delete(verificationToken);
            return "인증 시간이 초과 되었습니다.";
        }
        if(member == null) throw new Exception("changePassword(),member가 조회되지 않음");
        member.setPassword(passwordEncoder.encode(password));
        memberRepository.save(member);
        return "비밀번호가 변경되었습니다.";
    }

}
