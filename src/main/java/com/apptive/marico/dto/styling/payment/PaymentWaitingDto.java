package com.apptive.marico.dto.styling.payment;

import com.apptive.marico.entity.Member;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentWaitingDto {
    List<PaymentWaitingMemberDto> paymentWaitingMembers = new ArrayList<>();

    static public PaymentWaitingDto toDto(List<PaymentWaitingMemberDto> paymentWaitingMembers){
        return PaymentWaitingDto.builder()
                .paymentWaitingMembers(paymentWaitingMembers)
                .build();
    }
}
