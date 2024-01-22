package com.apptive.marico.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ServiceApplicationDto {

    private AccountDto refundAccount;
    private AccountDto stylistAccount;
    private int price;

}
