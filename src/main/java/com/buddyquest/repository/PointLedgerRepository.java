package com.buddyquest.repository;

import com.buddyquest.model.PointLedger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PointLedgerRepository extends JpaRepository<PointLedger, UUID> {

    // уже есть extends JpaRepository...

    // новый метод: топ 10 по убыванию очков
    List<PointLedger> findTop10ByOrderByPointsDesc();
}
