package br.com.finduhouse.entity;

import java.util.Locale;

import br.com.finduhouse.dto.DenunciaDtos;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class DenunciaMotivoConverter
        implements AttributeConverter<DenunciaDtos.Motivo, String> {

    @Override
    public String convertToDatabaseColumn(DenunciaDtos.Motivo motivo) {
        return motivo == null ? null : motivo.name().toLowerCase(Locale.ROOT);
    }

    @Override
    public DenunciaDtos.Motivo convertToEntityAttribute(String value) {
        return value == null
                ? null
                : DenunciaDtos.Motivo.valueOf(value.toUpperCase(Locale.ROOT));
    }
}
