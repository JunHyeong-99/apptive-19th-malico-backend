package com.apptive.marico.repository;

import com.apptive.marico.entity.Member;
import com.apptive.marico.entity.StylistService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StylistServiceRepository extends JpaRepository<StylistService, Long> {


    @Query("SELECT s FROM StylistService s JOIN FETCH s.serviceCategory WHERE s.stylist.id = :stylist_id")
    List<StylistService> findAllByStylistId(Long stylist_id);

    @Query("SELECT s FROM StylistService s JOIN FETCH s.serviceCategory WHERE s.stylist.userId = :userId")
    List<StylistService> findAllByStylistUserId(String userId);
    int countByStylist_id(Long stylist_id);

    @Query("SELECT s FROM StylistService s JOIN FETCH s.stylist WHERE s.id = :service_id")
    Optional<StylistService> findServiceWithStylistById(Long service_id);

}
