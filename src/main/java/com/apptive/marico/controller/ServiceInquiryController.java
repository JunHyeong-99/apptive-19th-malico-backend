package com.apptive.marico.controller;

import com.apptive.marico.dto.stylist.StylistMypageEditDto;
import com.apptive.marico.dto.stylistService.InquiryDto;
import com.apptive.marico.service.InquiryService;
import com.apptive.marico.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/service/inquiry")
@RequiredArgsConstructor
public class ServiceInquiryController {

    private final InquiryService inquiryService;
    @PostMapping
    public ResponseEntity<?> addInquiry(Principal principal, @RequestBody InquiryDto inquiryDto) {
        return ResponseEntity.ok(ApiUtils.success(inquiryService.addInquiry(principal.getName(), inquiryDto)));
    }

    @GetMapping
    public ResponseEntity<?> loadInquiryList(Principal principal) {
        return ResponseEntity.ok(ApiUtils.success(inquiryService.loadInquiryList(principal.getName())));
    }

    @GetMapping("/stylist/{inquiry_id}")
    public ResponseEntity<?> loadStylistInquiry(Principal principal, @PathVariable Long inquiry_id) {
        return ResponseEntity.ok(ApiUtils.success(inquiryService.loadStylistInquiry(principal.getName(), inquiry_id)));
    }

    @PostMapping(value = "/stylist/{inquiry_id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> addStylistInquiryAnswer(Principal principal,@PathVariable Long inquiry_id, @RequestPart InquiryDto.InquiryAnswerContentDto answerContentDto ,@RequestPart List<MultipartFile> answerImgs) {
        System.out.println(answerImgs.get(0).getOriginalFilename());
        System.out.println(answerContentDto.getResponseContent());
        return ResponseEntity.ok(ApiUtils.success(inquiryService.addInquiryAnswer(principal.getName(), inquiry_id, answerContentDto.getResponseContent(), answerImgs)));
    }

    @GetMapping("/member/{inquiry_id}")
    public ResponseEntity<?> loadMemberInquiry(Principal principal, @PathVariable Long inquiry_id) {
        return ResponseEntity.ok(ApiUtils.success(inquiryService.loadMemberInquiry(principal.getName(), inquiry_id)));
    }
}
