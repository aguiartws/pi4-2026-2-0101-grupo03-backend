package br.com.finduhouse.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "favorito")
public class Favorito {

    @EmbeddedId
    private FavoritoId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("estudanteId")
    @JoinColumn(name = "estudante_id", nullable = false)
    private Estudante estudante;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("vagaId")
    @JoinColumn(name = "vaga_id", nullable = false)
    private Vaga vaga;

    protected Favorito() {
    }

    public Favorito(Estudante estudante, Vaga vaga) {
        this.id = new FavoritoId(estudante.getId(), vaga.getId());
        this.estudante = estudante;
        this.vaga = vaga;
    }

    public FavoritoId getId() {
        return id;
    }

    public Vaga getVaga() {
        return vaga;
    }
}
