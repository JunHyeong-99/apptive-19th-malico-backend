package com.apptive.marico.service.auth;

import com.apptive.marico.dto.findPwd.NewPwdRequestDto;
import com.apptive.marico.dto.stylist.StylistRequestDto;
import com.apptive.marico.dto.stylist.StylistResponseDto;
import com.apptive.marico.entity.Member;
import com.apptive.marico.entity.Role;
import com.apptive.marico.entity.Stylist;
import com.apptive.marico.entity.token.VerificationToken;
import com.apptive.marico.exception.CustomException;
import com.apptive.marico.repository.*;
import com.apptive.marico.service.VerificationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;

import static com.apptive.marico.entity.Role.RoleName.ROLE_STYLIST;
import static com.apptive.marico.exception.ErrorCode.*;

@Service
@Transactional
@RequiredArgsConstructor
public class StylistAuthService {
    private final StylistRepository stylistRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    private final VerificationTokenRepository verificationTokenRepository;

    private final CustomUserDetailsService customUserDetailsService;
    private final VerificationTokenService verificationTokenService;

    @Transactional
    public StylistResponseDto signup(StylistRequestDto stylistRequestDto) {
        customUserDetailsService.checkAvailability(stylistRequestDto);


        Role userRole = roleRepository.findByName(ROLE_STYLIST)
                .orElseThrow(() -> new CustomException(ROLE_NOT_FOUND));

        Stylist stylist = stylistRequestDto.toStylist(passwordEncoder);
        stylist.setRoles(Collections.singleton(userRole));

        return StylistResponseDto.toDto(stylistRepository.save(stylist));
    }



    public String changePassword(Stylist stylist, NewPwdRequestDto newPwdRequestDto){
        VerificationToken verificationToken = verificationTokenRepository.findByVerificationCode(newPwdRequestDto.getCode());
        if (verificationToken == null) {
            throw new CustomException(VERIFICATION_CODE_INVAILD);
        }
        if(!verificationToken.getExpiryDate().isAfter(LocalDateTime.now())) {
            verificationTokenRepository.delete(verificationToken);
            throw new CustomException(VERIFICATION_CODE_TIMEOUT);
        }
        if(stylist == null) throw new CustomException(STYLIST_NOT_FOUND);
        verificationTokenRepository.delete(verificationToken);
        stylist.setPassword(passwordEncoder.encode(newPwdRequestDto.getPassword()));
        stylistRepository.save(stylist);
        return "비밀번호가 변경되었습니다.";
    }


}
