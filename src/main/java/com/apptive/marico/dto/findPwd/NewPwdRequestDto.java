package com.apptive.marico.dto.findPwd;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewPwdRequestDto {
    private String code;
    private String userId;
    private String password;

}
