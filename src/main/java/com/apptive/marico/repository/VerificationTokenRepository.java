package com.apptive.marico.repository;

import com.apptive.marico.entity.token.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    VerificationToken findByVerificationCode(String verificationCode);
}
