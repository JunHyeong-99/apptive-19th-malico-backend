package com.apptive.marico.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "educations")
public class Education {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "education_id")
    private Long id;

    // 대학
    @Column(nullable = false)
    private String university;

    // 학과
    @Column(nullable = false)
    private String major;

    @Column(nullable = false)
    private String admissionYear;

    @Column(nullable = false)
    private String graduationYear;

    @OneToOne(mappedBy = "education", fetch = FetchType.LAZY)
    private Stylist stylist;
}
