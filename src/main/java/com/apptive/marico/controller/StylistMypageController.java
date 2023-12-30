package com.apptive.marico.controller;

import com.apptive.marico.dto.stylist.StylistMypageDto;
import com.apptive.marico.dto.stylist.StylistMypageEditDto;
import com.apptive.marico.dto.stylist.service.StylistServiceDto;
import com.apptive.marico.service.StylistMypageService;
import com.apptive.marico.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/mypage/stylist")
@RequiredArgsConstructor
public class StylistMypageController {
    private final StylistMypageService stylistMypageService;

    // 마이페이지 조회
    @GetMapping
    public ResponseEntity<StylistMypageDto> mypage(Principal principal) {
        return ResponseEntity.ok(stylistMypageService.mypage(principal.getName()));
    }

    @GetMapping("/information")
    public ResponseEntity<StylistMypageEditDto> loadInf(Principal principal) {
        return ResponseEntity.ok(stylistMypageService.getInformation(principal.getName()));
    }

    @PostMapping("/information")
    public ResponseEntity<?> editInf(Principal principal, @RequestBody StylistMypageEditDto stylistMypageEditDto) {
        return ResponseEntity.ok(new ApiUtils.ApiSuccess<>(stylistMypageService.editInformation(principal.getName(), stylistMypageEditDto)));
    }

    @GetMapping("/stylist-service")
    public ResponseEntity<?> loadService(Principal principal) {
        return ResponseEntity.ok(stylistMypageService.getService(principal.getName()));
    }

    @PostMapping("/stylist-service")
    public ResponseEntity<?> addService(Principal principal, @RequestBody StylistServiceDto stylistServiceDto) {
        System.out.println(stylistServiceDto);
        return ResponseEntity.ok(new ApiUtils.ApiSuccess<>(stylistMypageService.addService(principal.getName(), stylistServiceDto)));
    }
}