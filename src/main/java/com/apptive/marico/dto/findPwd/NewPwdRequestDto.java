package com.apptive.marico.dto.findPwd;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewPwdRequestDto {
    @NotNull
    private String code;
    @NotNull
    private String userId;
    private String password;

}
