package com.apptive.marico.repository;

import com.apptive.marico.entity.Member;
import com.apptive.marico.entity.Role;
import com.apptive.marico.entity.ServiceApplication;
import com.apptive.marico.entity.StylistService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceApplicationRepository extends JpaRepository<ServiceApplication, Long> {
    List<ServiceApplication> findByStylistService(StylistService stylistService);
    Optional<ServiceApplication> findByMember(Member member);
}
