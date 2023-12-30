package com.apptive.marico.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "services")
public class StylistService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_id")
    private Long id;

    private String serviceName;

    private String serviceDescription;

    private int price;

    @OneToMany(mappedBy = "stylistService", fetch = FetchType.EAGER) // 즉시 로딩
    private List<ServiceCategory> serviceCategory = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "stylist_id")
    private Stylist stylist;



}
