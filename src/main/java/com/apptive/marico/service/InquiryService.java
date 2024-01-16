package com.apptive.marico.service;

import com.apptive.marico.dto.stylistService.InquiryDto;
import com.apptive.marico.dto.stylistService.InquiryPreviewDto;
import com.apptive.marico.entity.Member;
import com.apptive.marico.entity.ServiceInquiry;
import com.apptive.marico.entity.Stylist;
import com.apptive.marico.entity.StylistService;
import com.apptive.marico.exception.CustomException;
import com.apptive.marico.repository.InquiryRepository;
import com.apptive.marico.repository.MemberRepository;
import com.apptive.marico.repository.StylistRepository;
import com.apptive.marico.repository.StylistServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.apptive.marico.exception.ErrorCode.*;

@Service
@Transactional
@RequiredArgsConstructor
public class InquiryService {

    private final MemberRepository memberRepository;
    private final InquiryRepository inquiryRepository;
    private final StylistServiceRepository stylistServiceRepository;
    private final StylistRepository stylistRepository;
    public String addInquiry(String userId, InquiryDto inquiryDto) {
        Member member = memberRepository.findByUserId(userId)
                            .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        StylistService stylistService = stylistServiceRepository.findById(inquiryDto.getService_id())
                                            .orElseThrow(() -> new CustomException(SERVICE_NOT_FOUND));
        inquiryRepository.save(ServiceInquiry.builder()
                                .title(inquiryDto.getTitle())
                                .content(inquiryDto.getContent())
                                .member(member)
                                .inquiryImg(inquiryDto.getImg())
                                .stylistService(stylistService)
                                .build());
        return "문의사항이 등록 되었습니다.";
    }

//    public InquiryDto loadInquiry(String userId) {
//
//    }

    public InquiryPreviewDto.DtoList loadInquiryList (String userId) {
        Optional<Member> member = memberRepository.findByUserId(userId);
        return member.map(m -> InquiryPreviewDto.DtoList.toDto(inquiryRepository.findByMember(m)))
                .orElseGet(() -> {
                    Stylist stylist = stylistRepository.findByUserId(userId)
                            .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

                    List<InquiryPreviewDto> inquiryPreviewDtoList = stylistServiceRepository
                            .findAllByStylistId(stylist.getId())
                            .stream()
                            .map(inquiryRepository::findByStylistService)
                            .flatMap(List::stream)
                            .map(InquiryPreviewDto::toDto)
                            .collect(Collectors.toList());

                    return InquiryPreviewDto.DtoList.builder()
                            .inquiryPreviewDtoList(inquiryPreviewDtoList)
                            .build();
                });
    }

    public InquiryDto loadMemberInquiry(String userId, Long inquiryId) {
        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        ServiceInquiry serviceInquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new CustomException(INQUIRY_NOT_FOUND));
        if(serviceInquiry.getMember() != member) {
            throw new CustomException(USER_AND_INQUIRY_NOT_MATCH);
        }
        return InquiryDto.toDto(serviceInquiry);
    }
    public InquiryDto loadStylistInquiry(String userId, Long inquiryId) {
        List<StylistService> stylistServiceList = stylistServiceRepository.findAllByStylistUserId(userId);
        ServiceInquiry serviceInquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new CustomException(INQUIRY_NOT_FOUND));
        StylistService stylistService = serviceInquiry.getStylistService();
        if (!stylistServiceList.contains(stylistService)) throw new CustomException(USER_AND_INQUIRY_NOT_MATCH);
        return InquiryDto.toDto(serviceInquiry);
    }
}
