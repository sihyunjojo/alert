package com.joyride.alert;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AlertRepository extends JpaRepository<Alert, Long> {
    @Query
    List<Alert> findPendingByMemberId(Long memberId);
}
