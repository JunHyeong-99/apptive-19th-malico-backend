package com.apptive.marico.controller;

import com.apptive.marico.dto.stylist.StylistMypageEditDto;
import com.apptive.marico.dto.stylistService.InquiryDto;
import com.apptive.marico.service.InquiryService;
import com.apptive.marico.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

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
    public ResponseEntity<?> loadInquiry(Principal principal) {
        return ResponseEntity.ok(ApiUtils.success(inquiryService.loadInquiryList(principal.getName())));
    }
}
