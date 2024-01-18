package com.apptive.marico.repository;

import com.apptive.marico.entity.Member;
import com.apptive.marico.entity.MyStyle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyStyleRepository extends JpaRepository<MyStyle,Long> {
}
