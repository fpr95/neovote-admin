package com.digiteo.neovoteIV.model.jpa.data;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Objects;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class ElectionStatusConverter implements AttributeConverter<ElectionStatus, String> {

    @Override
    public String convertToDatabaseColumn(ElectionStatus electionStatus) {
        if(electionStatus == null){
            return "NO INICIADO";
        }
        return electionStatus.getCode();
    }

    @Override
    public ElectionStatus convertToEntityAttribute(String code) {
        if(code == null){
            return ElectionStatus.NOT_INIT;
        }
        return Stream.of(ElectionStatus.values())
                .filter(s -> Objects.equals(s.getCode(), code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
