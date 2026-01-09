package com.example.ticketing.repository;

import com.example.ticketing.entity.ConcertInfo;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ConcertInfoRepository extends JpaRepository<ConcertInfo, Long> {
    // 🔒 락을 걸고 조회하는 메서드 추가
    // PESSIMISTIC_WRITE: "내가 수정할 거니까, 다른 애들은 읽지도 말고 쓰지도 말고 대기해!" (가장 강력한 락)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from ConcertInfo c where c.id = :id")
    Optional<ConcertInfo> findByIdWithLock(@Param("id") Long id);
}
