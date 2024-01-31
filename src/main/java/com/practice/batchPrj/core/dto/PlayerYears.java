package com.practice.batchPrj.core.dto;


import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.time.Year;

@Data
@ToString
public class PlayerYears implements Serializable {
    private Long playerId;
    private String playerName;
    private String teamName;
    private String position;
    private int age;
    private int years;

    public PlayerYears(Player player){
        this.playerId = player.getPlayerId();
        this.playerName = player.getPlayerName();
        this.teamName = player.getTeamName();
        this.position = player.getPosition();
        this.age = player.getAge();
        this.years = Year.now().getValue() - player.getAge();
    }
}
