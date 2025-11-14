package com.buddyquest.repository;

import com.buddyquest.model.PointLedger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PointLedgerRepository extends JpaRepository<PointLedger, UUID> {
}

