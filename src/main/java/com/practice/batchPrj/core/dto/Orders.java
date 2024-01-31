package com.practice.batchPrj.core.dto;

import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@ToString
@Entity
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "order_id", nullable = false)
    private Integer id;
    private String customerName;
    private LocalDate orderDate;
    private BigDecimal totalAmount;

}
