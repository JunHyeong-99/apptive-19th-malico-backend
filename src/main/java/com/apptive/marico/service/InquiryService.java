package com.apptive.marico.service;

import com.apptive.marico.dto.stylistService.InquiryDto;
import com.apptive.marico.dto.stylistService.InquiryPreviewDto;
import com.apptive.marico.dto.stylistService.InquiryPreviewListDto;
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

import static com.apptive.marico.exception.ErrorCode.SERVICE_NOT_FOUND;
import static com.apptive.marico.exception.ErrorCode.USER_NOT_FOUND;

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

    public InquiryPreviewListDto loadInquiryList (String userId) {
        Optional<Member> member = memberRepository.findByUserId(userId);

        return member.map(m -> InquiryPreviewListDto.toDto(inquiryRepository.findByMember(m)))
                .orElseGet(() -> {
                    Stylist stylist = stylistRepository.findByUserId(userId)
                            .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

                    List<InquiryPreviewDto> inquiryPreviewDtoList = stylistServiceRepository
                            .findAllByStylist_id(stylist.getId())
                            .stream()
                            .map(inquiryRepository::findByStylistService)
                            .flatMap(List::stream)
                            .map(InquiryPreviewDto::toDto)
                            .collect(Collectors.toList());

                    return InquiryPreviewListDto.builder()
                            .inquiryPreviewDtoList(inquiryPreviewDtoList)
                            .build();
                });
    }
}
