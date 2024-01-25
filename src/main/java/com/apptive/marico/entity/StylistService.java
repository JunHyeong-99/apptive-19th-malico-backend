package com.apptive.marico.entity;

import com.apptive.marico.dto.stylist.service.StylistServiceDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
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

    @OneToOne(mappedBy = "stylistService", cascade = CascadeType.REMOVE)
    private ServiceCategory serviceCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stylist_id")
    private Stylist stylist;

    @OneToMany(mappedBy = "stylistService", cascade = CascadeType.REMOVE)
    private List<ServiceInquiry> serviceInquiries = new ArrayList<>();

    public void editService(StylistServiceDto stylistServiceDto){
        this.serviceName = stylistServiceDto.getServiceName();
        this.serviceDescription = stylistServiceDto.getServiceDescription();
        this.price = stylistServiceDto.getPrice();
    }

}
