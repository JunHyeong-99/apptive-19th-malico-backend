package com.apptive.marico.service;

import com.apptive.marico.dto.member.MemberResponseDto;
import com.apptive.marico.entity.Member;
import com.apptive.marico.entity.Stylist;
import com.apptive.marico.repository.MemberRepository;
import com.apptive.marico.repository.StylistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

/**
 * username을 가지고 사용자 정보를 조회하고 session에 저장될 사용자 주체 정보인 UserDetails를 반환하는 Interface
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final StylistRepository stylistRepository;


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 동시에 두 타입의 사용자를 찾기
        Optional<UserDetails> memberUserDetails = memberRepository.findByUsername(username)
                .map(this::createUserDetails);
        Optional<UserDetails> stylistUserDetails = stylistRepository.findByUsername(username)
                .map(this::createUserDetails);

        // Member가 존재하면 Member 반환, 아니면 Stylist 확인
        return memberUserDetails.orElseGet(() -> stylistUserDetails.orElseThrow(() ->
                new UsernameNotFoundException(username + " -> 데이터베이스에서 찾을 수 없습니다.")));
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

}
