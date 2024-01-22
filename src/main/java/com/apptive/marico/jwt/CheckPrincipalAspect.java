package com.apptive.marico.jwt;

import com.apptive.marico.exception.CustomException;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.security.Principal;

import static com.apptive.marico.exception.ErrorCode.AUTHORIZATION_NOT_FOUND;

@Aspect
@Component
public class CheckPrincipalAspect {

    @Before("execution(* com.apptive.marico.controller..*.*(..)) && args(principal,..)")
    public void checkPrincipal(Principal principal) {
        if (principal == null) {
            throw new CustomException(AUTHORIZATION_NOT_FOUND);
        }
    }
}