package com.apptive.marico.repository;

import com.apptive.marico.entity.Career;
import com.apptive.marico.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface CareerRepository extends JpaRepository<Career, Long> {
    @Transactional
    void deleteByStylist_Id(Long stylistId);

    // 일반 SQL쿼리
//    @Transactional
//    @Query(value = "delete from careers where stylist_id = :stylistId", nativeQuery = true)
//    void deleteByStylist_Id_Qerry(@Param("stylistId") Long stylistId);
}
