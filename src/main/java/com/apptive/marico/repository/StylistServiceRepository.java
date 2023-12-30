package com.apptive.marico.repository;

import com.apptive.marico.entity.StylistService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StylistServiceRepository extends JpaRepository<StylistService, Long> {


    @Query("SELECT s FROM StylistService s JOIN FETCH s.serviceCategory")
    List<StylistService> findAllByStylist_id(Long stylist_id);

    int countByStylist_id(Long stylist_id);

}
