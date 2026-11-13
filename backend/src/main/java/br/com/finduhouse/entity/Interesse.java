package br.com.finduhouse.entity;

import java.time.LocalDateTime;

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
@Table(name = "interesse")
public class Interesse {

    public enum StatusEnvio {
        PENDENTE,
        ENVIADO,
        FALHOU
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "estudante_id", nullable = false)
    private Estudante estudante;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vaga_id", nullable = false)
    private Vaga vaga;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    @Convert(converter = InteresseStatusEnvioConverter.class)
    @Column(name = "status_envio_estudante", nullable = false, length = 20)
    private StatusEnvio statusEnvioEstudante;

    @Convert(converter = InteresseStatusEnvioConverter.class)
    @Column(name = "status_envio_proprietario", nullable = false, length = 20)
    private StatusEnvio statusEnvioProprietario;

    protected Interesse() {
    }

    public Interesse(Estudante estudante, Vaga vaga) {
        this.estudante = estudante;
        this.vaga = vaga;
        this.statusEnvioEstudante = StatusEnvio.PENDENTE;
        this.statusEnvioProprietario = StatusEnvio.PENDENTE;
    }

    @PrePersist
    void aoCriar() {
        criadoEm = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Estudante getEstudante() {
        return estudante;
    }

    public Vaga getVaga() {
        return vaga;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public StatusEnvio getStatusEnvioEstudante() {
        return statusEnvioEstudante;
    }

    public StatusEnvio getStatusEnvioProprietario() {
        return statusEnvioProprietario;
    }

    public void marcarEnvioEstudante(StatusEnvio status) {
        statusEnvioEstudante = status;
    }

    public void marcarEnvioProprietario(StatusEnvio status) {
        statusEnvioProprietario = status;
    }
}
