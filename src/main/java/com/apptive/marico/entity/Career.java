package com.apptive.marico.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "careers")
public class Career {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "career_id")
    private Long id;

    // 대학
    @Column(nullable = false)
    private String organizationName;

    // 학과
    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String startYear;

    @Column(nullable = false)
    private String endYear;

    @OneToOne(mappedBy = "career", fetch = FetchType.LAZY)
    private Stylist stylist;

}
