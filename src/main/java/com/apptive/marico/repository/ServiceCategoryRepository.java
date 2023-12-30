package com.apptive.marico.repository;

import com.apptive.marico.entity.ServiceCategory;
import com.apptive.marico.entity.StylistService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceCategoryRepository extends JpaRepository<ServiceCategory, Long> {
}
