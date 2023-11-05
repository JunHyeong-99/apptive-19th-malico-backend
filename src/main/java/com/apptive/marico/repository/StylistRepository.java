package com.apptive.marico.repository;

import com.apptive.marico.entity.Member;
import com.apptive.marico.entity.Stylist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StylistRepository extends JpaRepository<Stylist,Long> {
    Optional<Stylist> findByUsername(String username);
    boolean existsByUsername(String username);
}
