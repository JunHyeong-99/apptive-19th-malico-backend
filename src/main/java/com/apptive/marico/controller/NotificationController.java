package com.apptive.marico.controller;

import com.apptive.marico.dto.NoticeDto;
import com.apptive.marico.service.NoticeService;
import com.apptive.marico.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NoticeService noticeService;

    @GetMapping("/notice")
    public ResponseEntity<?> loadNoticeList(Principal principal) {
        return ResponseEntity.ok(noticeService.loadNoticeList(principal.getName()));
    }

    @GetMapping("/notice/{notice_id}")
    public ResponseEntity<?> loadNotice(Principal principal, @PathVariable Long notice_id) {
        return ResponseEntity.ok(new ApiUtils.ApiSuccess<>(noticeService.loadNotice(principal.getName(), notice_id)));
    }

    @PostMapping("/notice")
    public ResponseEntity<?> addNotice(Principal principal, @RequestBody NoticeDto noticeDto) {
        return ResponseEntity.ok(new ApiUtils.ApiSuccess<>(noticeService.addNotice(principal, noticeDto)));
    }


}
