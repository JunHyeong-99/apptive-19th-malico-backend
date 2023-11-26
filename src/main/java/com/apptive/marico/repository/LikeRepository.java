package com.apptive.marico.repository;

import com.apptive.marico.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    List<Like> findAllByMemberId(Long memberId);
}
