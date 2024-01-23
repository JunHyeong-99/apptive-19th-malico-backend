package com.apptive.marico.repository;

import com.apptive.marico.entity.Style;
import com.apptive.marico.entity.Stylist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StyleRepository extends JpaRepository<Style,Long> {
}
