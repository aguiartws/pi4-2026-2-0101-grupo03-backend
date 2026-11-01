package br.com.finduhouse.entity;

import java.util.Locale;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class VagaStatusConverter implements AttributeConverter<Vaga.Status, String> {

    @Override
    public String convertToDatabaseColumn(Vaga.Status status) {
        return status == null ? null : status.name().toLowerCase(Locale.ROOT);
    }

    @Override
    public Vaga.Status convertToEntityAttribute(String value) {
        return value == null ? null : Vaga.Status.valueOf(value.toUpperCase(Locale.ROOT));
    }
}
