package com.apptive.marico.entity.token;


import com.apptive.marico.entity.Member;
import com.apptive.marico.entity.Stylist;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


//사용하지 않는 방식
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "verificationToken")
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String verificationCode;
    private LocalDateTime expiryDate;

    @OneToOne(targetEntity = Member.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "member_id")
    private Member member;

    @OneToOne(targetEntity = Stylist.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "stylist_id")
    private Stylist stylist;

}
