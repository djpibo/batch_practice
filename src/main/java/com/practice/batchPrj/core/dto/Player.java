package com.practice.batchPrj.core.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@Builder
public class Player implements Serializable {
    private Long playerId;
    private String playerName;
    private String teamName;
    private String position;
    private int age;
}
