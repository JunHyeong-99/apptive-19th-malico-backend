package com.apptive.marico.service;

import com.apptive.marico.dto.LoginDto;
import com.apptive.marico.dto.member.MemberResponseDto;
import com.apptive.marico.dto.token.TokenRequestDto;
import com.apptive.marico.dto.token.TokenResponseDto;
import com.apptive.marico.entity.Member;
import com.apptive.marico.entity.Stylist;
import com.apptive.marico.entity.token.RefreshToken;
import com.apptive.marico.exception.CustomException;
import com.apptive.marico.jwt.TokenProvider;
import com.apptive.marico.repository.MemberRepository;
import com.apptive.marico.repository.RefreshTokenRepository;
import com.apptive.marico.repository.StylistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

import static com.apptive.marico.exception.ErrorCode.ALREADY_SAVED_EMAIL;
import static com.apptive.marico.exception.ErrorCode.USER_NOT_FOUND;

/**
 * username을 가지고 사용자 정보를 조회하고 session에 저장될 사용자 주체 정보인 UserDetails를 반환하는 Interface
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final StylistRepository stylistRepository;

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userId) {
        Optional<Member> memberOptional = memberRepository.findByUserId(userId);
        Optional<Stylist> stylistOptional = stylistRepository.findByUserId(userId);
        if (memberOptional.isPresent()) {
            return createUserDetails(memberOptional.get());
        } else if (stylistOptional.isPresent()) {
            return createUserDetails(stylistOptional.get());
        } else {
            throw new CustomException(USER_NOT_FOUND);
        }
    }

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

    @Transactional
    public void logout(TokenRequestDto tokenReqDto) {
        // 로그아웃하려는 사용자의 정보를 가져옴
        Authentication authentication = tokenProvider.getAuthentication(tokenReqDto.getAccessToken());

        // 저장소에서 해당 사용자의 refresh token 삭제
        refreshTokenRepository.deleteByKey(authentication.getName());
    }

    // DB 에 Member 값이 존재한다면 UserDetails 객체로 만들어서 리턴
    private UserDetails createUserDetails(Member member) {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.getAuthorities().toString());
        return new org.springframework.security.core.userdetails.User(
            member.getUsername(),
            member.getPassword(),
            Collections.singleton(grantedAuthority)
        );
    }

    private UserDetails createUserDetails(Stylist stylist) {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(stylist.getAuthorities().toString());
        return new org.springframework.security.core.userdetails.User(
                stylist.getUsername(),
                stylist.getPassword(),
                Collections.singleton(grantedAuthority)
        );
    }

    public void checkEmailAvailability(String email) {
        if (stylistRepository.existsByUserId(email) || memberRepository.existsByUserId(email)) {
            throw new CustomException(ALREADY_SAVED_EMAIL);
        }
    }
}
