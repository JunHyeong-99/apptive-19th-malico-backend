package com.apptive.marico.repository;

import com.apptive.marico.entity.Member;
import com.apptive.marico.entity.Stylist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StylistRepository extends JpaRepository<Stylist,Long> {
    Optional<Stylist> findByUserId(String userId);
    Optional<Stylist> findByEmail(String email);
    boolean existsByUserId(String userId);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);

    @Query("SELECT DISTINCT s FROM Stylist s JOIN FETCH s.style WHERE s.userId = :userId")
    Optional<Stylist> findByUserIdWithStyle(String userId);
}
