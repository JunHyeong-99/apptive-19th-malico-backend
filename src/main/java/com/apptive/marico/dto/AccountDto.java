package com.apptive.marico.dto;

import com.apptive.marico.entity.Career;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AccountDto {
    private String bank;
    private String accountNumber;
    private String accountHolder;
}
