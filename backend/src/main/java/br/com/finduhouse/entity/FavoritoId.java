package br.com.finduhouse.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;

@Embeddable
public class FavoritoId implements Serializable {

    private Long estudanteId;
    private Long vagaId;

    protected FavoritoId() {
    }

    public FavoritoId(Long estudanteId, Long vagaId) {
        this.estudanteId = estudanteId;
        this.vagaId = vagaId;
    }

    public Long getEstudanteId() {
        return estudanteId;
    }

    public Long getVagaId() {
        return vagaId;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof FavoritoId that)) {
            return false;
        }
        return Objects.equals(estudanteId, that.estudanteId)
                && Objects.equals(vagaId, that.vagaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(estudanteId, vagaId);
    }
}
