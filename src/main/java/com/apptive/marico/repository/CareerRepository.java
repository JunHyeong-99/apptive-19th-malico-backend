package com.apptive.marico.repository;

import com.apptive.marico.entity.Career;
import com.apptive.marico.entity.Like;
import com.apptive.marico.entity.Stylist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface CareerRepository extends JpaRepository<Career, Long> {
    @Modifying //delete sql career수 만큼 나가던 걸 하나만 나가게 수정
    @Query("DELETE FROM Career c WHERE c.stylist = :stylist")
    void deleteByStylist(Stylist stylist);

}
