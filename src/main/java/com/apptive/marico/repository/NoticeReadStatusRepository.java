package com.apptive.marico.repository;

import com.apptive.marico.entity.NoticeReadStatus;
import com.apptive.marico.entity.Stylist;
import com.apptive.marico.entity.token.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NoticeReadStatusRepository extends JpaRepository<NoticeReadStatus, Long> {

    @Query("SELECT n FROM NoticeReadStatus n JOIN FETCH n.notice WHERE n.stylist.userId = :userId")
    List<NoticeReadStatus> findByStylistId(String userId);

    @Query("SELECT n FROM NoticeReadStatus n JOIN FETCH n.notice WHERE n.member.userId = :userId")
    List<NoticeReadStatus> findByMemberId(String userId);

    @Modifying
    @Query("DELETE FROM NoticeReadStatus n where n.notice.id = :notice_id")
    void deleteByNoticeId(Long notice_id);
}
