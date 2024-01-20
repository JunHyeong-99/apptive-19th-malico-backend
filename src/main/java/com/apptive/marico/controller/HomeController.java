package com.apptive.marico.controller;


import com.apptive.marico.dto.stylistService.StylistFilterDto;
import com.apptive.marico.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/home")
@RequiredArgsConstructor
public class HomeController {

    private final HomeService homeService;
    @GetMapping("/filter")
    public ResponseEntity<?> stylistFilter(@RequestBody StylistFilterDto stylistFilterDto) {
        return ResponseEntity.ok(homeService.filter(stylistFilterDto));
    }

    @GetMapping("/stylist/{stylist_id}")
    public ResponseEntity<?> stylistDetail(@PathVariable Long stylist_id) {
        return ResponseEntity.ok(homeService.stylistDetail(stylist_id));
    }
}
