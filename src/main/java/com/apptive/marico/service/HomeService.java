package com.apptive.marico.service;

import com.apptive.marico.dto.AccountDto;
import com.apptive.marico.dto.ServiceApplicationDto;
import com.apptive.marico.dto.stylist.FilterStyleDto;
import com.apptive.marico.dto.stylist.StylistDetailDto;
import com.apptive.marico.dto.stylistService.StylistFilterDto;
import com.apptive.marico.entity.*;
import com.apptive.marico.exception.CustomException;
import com.apptive.marico.repository.MemberRepository;
import com.apptive.marico.repository.ServiceApplicationRepository;
import com.apptive.marico.repository.StylistRepository;
import com.apptive.marico.repository.StylistServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.apptive.marico.exception.ErrorCode.SERVICE_NOT_FOUND;
import static com.apptive.marico.exception.ErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HomeService {

    private final StylistRepository stylistRepository;
    private final MemberRepository memberRepository;
    private final StylistServiceRepository stylistServiceRepository;
    private final ServiceApplicationRepository serviceApplicationRepository;

    public List<FilterStyleDto> filter(StylistFilterDto stylistFilterDto) {
        List<Style> filteredStyle = stylistRepository.findAllWithStyle().stream()
                .filter(stylist ->
                        (Objects.equals(stylistFilterDto.getGender(), "All") || Objects.equals(String.valueOf(stylist.getGender()), stylistFilterDto.getGender())) &&
                                (Objects.equals(stylistFilterDto.getCity(), "All") || Objects.equals(stylist.getCity(), stylistFilterDto.getCity()))
                )
                .flatMap(stylist -> stylist.getStyles().stream())
                .filter(style -> Objects.equals(stylistFilterDto.getStyle(), "All") || Objects.equals(style.getCategory(), stylistFilterDto.getStyle()))
                .toList();

        return filteredStyle.stream().map(FilterStyleDto::toDto).collect(Collectors.toList());

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

    @Transactional
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
