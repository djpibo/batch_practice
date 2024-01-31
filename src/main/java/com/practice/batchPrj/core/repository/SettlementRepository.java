package com.practice.batchPrj.core.repository;

import com.practice.batchPrj.core.dto.Settlements;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettlementRepository extends JpaRepository<Settlements, Integer> {
}
