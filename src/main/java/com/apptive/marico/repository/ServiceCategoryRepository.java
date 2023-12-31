package com.apptive.marico.repository;

import com.apptive.marico.entity.ServiceCategory;
import com.apptive.marico.entity.Stylist;
import com.apptive.marico.entity.StylistService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ServiceCategoryRepository extends JpaRepository<ServiceCategory, Long> {

    @Modifying
    @Query("DELETE FROM ServiceCategory s WHERE s.stylistService = :stylistService")
    void deleteAllByStylistService(StylistService stylistService);
}
