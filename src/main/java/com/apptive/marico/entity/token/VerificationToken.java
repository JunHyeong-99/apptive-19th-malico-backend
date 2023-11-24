package com.apptive.marico.entity.token;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "verificationToken")
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String verificationCode;
    private LocalDateTime expiryDate;
    private String email;

    public VerificationToken(String email) {
        this.verificationCode = generateVerificationCode();
        this.expiryDate = calculateExpiryDate();
        this.email = email;
    }

    public static VerificationToken create(String email) {
        return new VerificationToken(email);
    }

    private String generateVerificationCode() {
        String verificationCode = UUID.randomUUID().toString();
        verificationCode = verificationCode.replaceAll("-", "");
        return verificationCode.substring(0, 6);
    }

    private LocalDateTime calculateExpiryDate() {
        return LocalDateTime.now().plusMinutes(3);
    }
}
