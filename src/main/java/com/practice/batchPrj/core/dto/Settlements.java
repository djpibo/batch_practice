package com.practice.batchPrj.core.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@ToString
@Entity
@NoArgsConstructor
public class Settlements {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "settlement_id", nullable = false)
    private Integer id;
    private Integer orderId;
    private LocalDate settlementDate;
    private BigDecimal amount;

    public Settlements(Orders orders){
        this.orderId = orders.getId();
        this.settlementDate = LocalDate.now();
        this.amount = orders.getTotalAmount();
    }
}
