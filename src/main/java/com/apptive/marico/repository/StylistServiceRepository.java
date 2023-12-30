package com.apptive.marico.repository;

import com.apptive.marico.entity.StylistService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StylistServiceRepository extends JpaRepository<StylistService, Long> {

    List<StylistService> findAllByStylist_id(Long stylist_id);

    int countByStylist_id(Long stylist_id);

}
