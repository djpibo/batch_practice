package com.practice.batchPrj.core.repository;

import com.practice.batchPrj.core.dto.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Orders, Integer> {
}
