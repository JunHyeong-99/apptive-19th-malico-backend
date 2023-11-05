package com.apptive.marico.service;

import com.apptive.marico.dto.member.LoginDto;
import com.apptive.marico.dto.member.MemberRequestDto;
import com.apptive.marico.dto.member.MemberResponseDto;
import com.apptive.marico.dto.token.TokenResponseDto;
import com.apptive.marico.entity.Member;
import com.apptive.marico.entity.Role;
import com.apptive.marico.exception.CustomException;
import com.apptive.marico.jwt.TokenProvider;
import com.apptive.marico.repository.MemberRepository;
import com.apptive.marico.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collections;

import static com.apptive.marico.entity.Role.RoleName.ROLE_USER;
import static com.apptive.marico.exception.ErrorCode.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public MemberResponseDto signup(MemberRequestDto memberRequestDto) {
        Role userRole = roleRepository.findByName(ROLE_USER).orElseThrow(
                () -> new CustomException(ROLE_NOT_FOUND));

        if (memberRepository.existsByUsername(memberRequestDto.getEmail())) {
            throw new CustomException(ALREADY_SAVED_EMAIL);
        }

        Member member = memberRequestDto.toMember(passwordEncoder);
        member.setRoles(Collections.singleton(userRole));

        return MemberResponseDto.toDto(memberRepository.save(member));
    }
}
