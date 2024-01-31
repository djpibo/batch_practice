package com.practice.batchPrj.core.mapper;

import com.practice.batchPrj.core.dto.Player;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class PlayerFieldSetMapper implements FieldSetMapper<Player> {
    @Override
    public Player mapFieldSet(FieldSet fieldSet) throws BindException {
        return Player.builder()
                .playerId(fieldSet.readLong(0))
                .playerName(fieldSet.readString(1))
                .teamName(fieldSet.readString(2))
                .position(fieldSet.readString(3))
                .age(fieldSet.readInt(4))
                .build();
    }
}
