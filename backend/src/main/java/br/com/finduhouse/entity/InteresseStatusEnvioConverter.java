package br.com.finduhouse.entity;

import java.util.Locale;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class InteresseStatusEnvioConverter
        implements AttributeConverter<Interesse.StatusEnvio, String> {

    @Override
    public String convertToDatabaseColumn(Interesse.StatusEnvio status) {
        return status == null ? null : status.name().toLowerCase(Locale.ROOT);
    }

    @Override
    public Interesse.StatusEnvio convertToEntityAttribute(String value) {
        return value == null
                ? null
                : Interesse.StatusEnvio.valueOf(value.toUpperCase(Locale.ROOT));
    }
}
