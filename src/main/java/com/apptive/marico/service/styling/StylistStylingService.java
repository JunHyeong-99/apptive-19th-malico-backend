package com.apptive.marico.service.styling;

import com.apptive.marico.dto.styling.MyClientDto;
import com.apptive.marico.dto.styling.MyClientMemberDto;
import com.apptive.marico.dto.styling.payment.PaymentWaitingDeatilDto;
import com.apptive.marico.dto.styling.payment.PaymentWaitingDto;
import com.apptive.marico.dto.styling.payment.PaymentWaitingMemberDto;
import com.apptive.marico.entity.*;
import com.apptive.marico.exception.CustomException;
import com.apptive.marico.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.apptive.marico.exception.ErrorCode.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StylistStylingService {
    private final MemberRepository memberRepository;
    private final StylistRepository stylistRepository;
    private final ServiceApplicationRepository serviceApplicationRepository;


    public MyClientDto findMyClient(String userId) {
        Stylist stylist = stylistRepository.findByUserId(userId).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND));

        List<MyClientMemberDto> myClientDtos = new ArrayList<>();
        for (StylistService stylistService : stylist.getStylistServices()) {
            List<ServiceApplication> serviceApplications = serviceApplicationRepository.findByStylistService(stylistService);
            for (ServiceApplication serviceApplication : serviceApplications) {
                if (serviceApplication.getApprovalStatus().equals("DONE")) {
                    myClientDtos.add(MyClientMemberDto.toDto(serviceApplication.getMember()));
                }
            }
        }
        return MyClientDto.toDto(myClientDtos);
    }

    public PaymentWaitingDto findPaymentWaitingList(String userId) {
        Stylist stylist = stylistRepository.findByUserId(userId).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND));

        List<PaymentWaitingMemberDto> paymentWaitingMembers = new ArrayList<>();
        for (StylistService stylistService : stylist.getStylistServices()) {
            List<ServiceApplication> serviceApplications = serviceApplicationRepository.findByStylistService(stylistService);
            if (serviceApplications != null) {
                for (ServiceApplication serviceApplication : serviceApplications) {
                    if (serviceApplication.getApprovalStatus().equals("WAITING")) {
                        Member member = serviceApplication.getMember();
                        paymentWaitingMembers.add(PaymentWaitingMemberDto.toDto(serviceApplication.getId(), member));
                    }
                }
            }
        }
        return PaymentWaitingDto.toDto(paymentWaitingMembers);
    }

    public PaymentWaitingDeatilDto findPaymentWaitingDetail(String userId, long serviceApplicationId) {
        Stylist stylist = stylistRepository.findByUserId(userId).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND));

        ServiceApplication serviceApplication = serviceApplicationRepository.findById(serviceApplicationId).orElseThrow(
                () -> new CustomException(SERVICE_APPLICATION_NOT_FOUND));
        Member member = serviceApplication.getMember();

        Set<String> preferredStyleCategoriesSet = new HashSet<>();
        for (Style style : member.getPreferredStyles()) {
            preferredStyleCategoriesSet.add(style.getCategory());
        }

        List<String> preferredStyleCategories = new ArrayList<>(preferredStyleCategoriesSet); // 중복 제거

        return PaymentWaitingDeatilDto.toDto(serviceApplication.getId(), serviceApplication.getMember(), preferredStyleCategories);
    }

    @Transactional
    public String paymentApproval(String userId, long serviceApplicationId) {
        Stylist stylist = stylistRepository.findByUserId(userId).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND));

        ServiceApplication serviceApplication = serviceApplicationRepository.findById(serviceApplicationId).orElseThrow(
                () -> new CustomException(SERVICE_APPLICATION_NOT_FOUND));

        if (serviceApplication.getApprovalStatus().equals("WAITING")) {
            serviceApplication.approval();
        } else{
            throw new CustomException(SERVICE_APPLICATION_NOT_WAITING);
        }
        return "결제 승인이 정상적으로 완료되었습니다.";
    }

    @Transactional
    public String paymentDenial(String userId, long serviceApplicationId) {
        Stylist stylist = stylistRepository.findByUserId(userId).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND));

        ServiceApplication serviceApplication = serviceApplicationRepository.findById(serviceApplicationId).orElseThrow(
                () -> new CustomException(SERVICE_APPLICATION_NOT_FOUND));

        if (serviceApplication.getApprovalStatus().equals("WAITING")) {
            serviceApplication.denial();
        }
        return "결제 거절이 정상적으로 완료되었습니다.";
    }
}
