package com.apptive.marico.service;

import com.apptive.marico.dto.LoginDto;
import com.apptive.marico.dto.member.MemberResponseDto;
import com.apptive.marico.dto.stylist.StylistRequestDto;
import com.apptive.marico.dto.stylist.StylistResponseDto;
import com.apptive.marico.dto.token.TokenRequestDto;
import com.apptive.marico.dto.token.TokenResponseDto;
import com.apptive.marico.entity.Member;
import com.apptive.marico.entity.Role;
import com.apptive.marico.entity.Stylist;
import com.apptive.marico.entity.token.RefreshToken;
import com.apptive.marico.exception.CustomException;
import com.apptive.marico.jwt.TokenProvider;
import com.apptive.marico.repository.RefreshTokenRepository;
import com.apptive.marico.repository.RoleRepository;
import com.apptive.marico.repository.StylistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

import static com.apptive.marico.entity.Role.RoleName.ROLE_STYLIST;
import static com.apptive.marico.exception.ErrorCode.ALREADY_SAVED_EMAIL;
import static com.apptive.marico.exception.ErrorCode.ROLE_NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StylistAuthService {
    private final StylistRepository stylistRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public StylistResponseDto signup(StylistRequestDto stylistRequestDto) {
        Role userRole = roleRepository.findByName(ROLE_STYLIST).orElseThrow(
                () -> new CustomException(ROLE_NOT_FOUND));

        if (stylistRepository.existsByUserId(stylistRequestDto.getEmail())) {
            throw new CustomException(ALREADY_SAVED_EMAIL);
        }

        Stylist stylist = stylistRequestDto.toStylist(passwordEncoder);
        stylist.setRoles(Collections.singleton(userRole));

        return StylistResponseDto.toDto(stylistRepository.save(stylist));
    }

    // 로그인에서 오류뜸 -> 자격 증명 실패
    // 서비스 사용자가 member와 stylist로 나뉘어지니까 CustomUserDetailsService의 loadUserByUsername 메소드를 어떻게 수정해야 할지 모르겠습니다!!
    @Transactional
    public TokenResponseDto login(LoginDto loginDto) {
        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = loginDto.toAuthentication();

        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //    authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenResponseDto tokenResDto = tokenProvider.generateTokenDto(authentication);

        // 4. RefreshToken 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenResDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        // 5. 토큰 발급
        return tokenResDto;
    }
}
