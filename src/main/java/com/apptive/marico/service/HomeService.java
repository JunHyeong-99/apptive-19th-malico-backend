package com.apptive.marico.service;

import com.apptive.marico.dto.AccountDto;
import com.apptive.marico.dto.ServiceApplicationDto;
import com.apptive.marico.dto.stylist.StylistDetailDto;
import com.apptive.marico.dto.stylistService.StylistFilterDto;
import com.apptive.marico.entity.Member;
import com.apptive.marico.entity.ServiceApplication;
import com.apptive.marico.entity.Stylist;
import com.apptive.marico.entity.StylistService;
import com.apptive.marico.exception.CustomException;
import com.apptive.marico.repository.MemberRepository;
import com.apptive.marico.repository.ServiceApplicationRepository;
import com.apptive.marico.repository.StylistRepository;
import com.apptive.marico.repository.StylistServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.apptive.marico.exception.ErrorCode.SERVICE_NOT_FOUND;
import static com.apptive.marico.exception.ErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional
public class HomeService {

    private final StylistRepository stylistRepository;
    private final MemberRepository memberRepository;
    private final StylistServiceRepository stylistServiceRepository;
    private final ServiceApplicationRepository serviceApplicationRepository;

    public String filter(StylistFilterDto stylistFilterDto) {
        return "0";
    }

    public StylistDetailDto stylistDetail(Long stylistId) {
        Stylist stylist = stylistRepository.findByIdWithService(stylistId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        return StylistDetailDto.toDto(stylist);
    }

    public ServiceApplicationDto loadApplication(String userId, Long serviceId) {
        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        StylistService stylistService = stylistServiceRepository.findServiceWithStylistById(serviceId)
                .orElseThrow(() -> new CustomException(SERVICE_NOT_FOUND));

        Stylist stylist = stylistService.getStylist();
        return ServiceApplicationDto.builder()
                .refundAccount(AccountDto.builder()
                        .bank(member.getBank())
                        .accountHolder(member.getAccountHolder())
                        .accountNumber(member.getAccountNumber())
                        .build())
                .stylistAccount(AccountDto.builder()
                        .bank(stylist.getBank())
                        .accountHolder(stylist.getAccountHolder())
                        .accountNumber(stylist.getAccountNumber())
                        .build())
                .build();
    }

    public String addApplication(String userId, Long serviceId) {
        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        StylistService stylistService = stylistServiceRepository.findServiceWithStylistById(serviceId)
                .orElseThrow(() -> new CustomException(SERVICE_NOT_FOUND));

        serviceApplicationRepository.save(ServiceApplication.builder()
                .member(member)
                .stylistService(stylistService)
                .build());
        return "서비스 신청이 정상적으로 접수되었습니다.";
    }
}
