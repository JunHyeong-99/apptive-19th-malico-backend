package com.apptive.marico.controller;


import com.apptive.marico.dto.stylistService.StylistFilterDto;
import com.apptive.marico.service.HomeService;
import com.apptive.marico.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/home")
@RequiredArgsConstructor
public class HomeController {

    private final HomeService homeService;
    @GetMapping("/filter")
    public ResponseEntity<?> stylistFilter(@RequestBody StylistFilterDto stylistFilterDto) {
        return ResponseEntity.ok(homeService.filter(stylistFilterDto));
    }

    // 스타일리스트 상세 페이지 조회
    @GetMapping("/stylist/{stylist_id}")
    public ResponseEntity<?> stylistDetail(@PathVariable Long stylist_id) {
        return ResponseEntity.ok(homeService.stylistDetail(stylist_id));
    }

    // 스타일리스트 서비스 신청 조회
    @GetMapping("/member/application/{service_id}")
    public ResponseEntity<?> loadService(Principal principal, @PathVariable Long service_id) {
        return ResponseEntity.ok(homeService.loadApplication(principal.getName(), service_id));
    }
    // 스타일리스트 서비스 신청 하기
    @PostMapping("/member/application/{service_id}")
    public ResponseEntity<?> addRequestService(Principal principal, @PathVariable Long service_id) {
        return ResponseEntity.ok(ApiUtils.success(homeService.addApplication(principal.getName(), service_id)));
    }

}
