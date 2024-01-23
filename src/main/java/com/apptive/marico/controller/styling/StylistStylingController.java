package com.apptive.marico.controller.styling;

import com.apptive.marico.service.styling.StylistStylingService;
import com.apptive.marico.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/styling/stylist")
@RequiredArgsConstructor
public class StylistStylingController {
    private final StylistStylingService stylistStylingService;

    // 담당 고객 조회
    @GetMapping("/")
    public ResponseEntity<?> findMyClient(Principal principal) {
        return ResponseEntity.ok(stylistStylingService.findMyClient(principal.getName()));
    }
    // 결제 승인 조회
    @GetMapping("/payment")
    public ResponseEntity<?> findPaymentWaitingList(Principal principal){
        return ResponseEntity.ok(stylistStylingService.findPaymentWaitingList(principal.getName()));
    }

    // 결제 승인 상세 조회
    @GetMapping("/payment/{serviceApplicationId}")
    public ResponseEntity<?> findPaymentWaitingDetail(Principal principal, @PathVariable long serviceApplicationId) {
        return ResponseEntity.ok(stylistStylingService.findPaymentWaitingDetail(principal.getName(), serviceApplicationId));
    }


    // 결제 승인
    @PostMapping("/payment/approval/{serviceApplicationId}")
    public ResponseEntity<?> paymentApproval(Principal principal, @PathVariable long serviceApplicationId) {
        return ResponseEntity.ok(new ApiUtils.ApiSuccess<>(stylistStylingService.paymentApproval(principal.getName(), serviceApplicationId)));
    }

    // 결제 거부
    @PostMapping("/payment/denial/{serviceApplicationId}")
    public ResponseEntity<?> paymentDenial(Principal principal, @PathVariable long serviceApplicationId) {
        return ResponseEntity.ok(new ApiUtils.ApiSuccess<>(stylistStylingService.paymentDenial(principal.getName(), serviceApplicationId)));
    }
}
