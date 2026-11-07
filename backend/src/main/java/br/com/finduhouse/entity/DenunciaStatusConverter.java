package br.com.finduhouse.entity;

import java.util.Locale;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class DenunciaStatusConverter
        implements AttributeConverter<Denuncia.Status, String> {

    @Override
    public String convertToDatabaseColumn(Denuncia.Status status) {
        return status == null ? null : status.name().toLowerCase(Locale.ROOT);
    }

    @Override
    public Denuncia.Status convertToEntityAttribute(String value) {
        return value == null ? null : Denuncia.Status.valueOf(value.toUpperCase(Locale.ROOT));
    }
}
