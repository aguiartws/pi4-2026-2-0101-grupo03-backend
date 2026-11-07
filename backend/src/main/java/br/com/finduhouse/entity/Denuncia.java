package br.com.finduhouse.entity;

import java.time.LocalDateTime;

import br.com.finduhouse.dto.DenunciaDtos;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "denuncia")
public class Denuncia {

    public enum Status {
        PENDENTE,
        EM_ANALISE,
        REVISADA
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vaga_id", nullable = false)
    private Vaga vaga;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "estudante_id", nullable = false)
    private Estudante estudante;

    @Convert(converter = DenunciaMotivoConverter.class)
    @Column(nullable = false)
    private DenunciaDtos.Motivo motivo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descricao;

    @Convert(converter = DenunciaStatusConverter.class)
    @Column(nullable = false)
    private Status status;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    protected Denuncia() {
    }

    public Denuncia(
            Vaga vaga,
            Estudante estudante,
            DenunciaDtos.Motivo motivo,
            String descricao) {
        this.vaga = vaga;
        this.estudante = estudante;
        this.motivo = motivo;
        this.descricao = descricao;
        this.status = Status.PENDENTE;
    }

    @PrePersist
    void aoCriar() {
        criadoEm = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Vaga getVaga() {
        return vaga;
    }

    public Estudante getEstudante() {
        return estudante;
    }

    public DenunciaDtos.Motivo getMotivo() {
        return motivo;
    }

    public String getDescricao() {
        return descricao;
    }

    public Status getStatus() {
        return status;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }
}
