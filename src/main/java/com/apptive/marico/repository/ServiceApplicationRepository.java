package com.apptive.marico.repository;

import com.apptive.marico.entity.Role;
import com.apptive.marico.entity.ServiceApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceApplicationRepository extends JpaRepository<ServiceApplication, Long> {
}
