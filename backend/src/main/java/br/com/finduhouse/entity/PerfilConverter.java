package br.com.finduhouse.entity;

import java.util.Locale;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class PerfilConverter implements AttributeConverter<Usuario.Perfil, String> {

    @Override
    public String convertToDatabaseColumn(Usuario.Perfil perfil) {
        // ESTUDANTE -> "estudante", como o CHECK do banco exige
        return perfil == null ? null : perfil.name().toLowerCase(Locale.ROOT);
    }

    @Override
    public Usuario.Perfil convertToEntityAttribute(String valor) {
        return valor == null ? null : Usuario.Perfil.valueOf(valor.toUpperCase(Locale.ROOT));
    }
}