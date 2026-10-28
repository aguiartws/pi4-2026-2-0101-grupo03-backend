package br.com.finduhouse.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Convert;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "vaga")
public class Vaga {

    public enum Status {
        ATIVA,
        EM_ANALISE,
        INATIVA
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "responsavel_id", nullable = false)
    private Usuario responsavel;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private String endereco;

    @Column(name = "valor_aluguel", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorAluguel;

    @Column(name = "numero_moradores", nullable = false)
    private Integer numeroMoradores;

    @Column(name = "regras_casa", nullable = false)
    private String regrasCasa;

    @Column(nullable = false, precision = 10, scale = 8)
    private BigDecimal latitude;

    @Column(nullable = false, precision = 11, scale = 8)
    private BigDecimal longitude;

    @Convert(converter = VagaStatusConverter.class)
    @Column(nullable = false)
    private Status status;

    @Column(name = "horario_dormir_semana", nullable = false)
    private String horarioDormirSemana;

    @Column(name = "horario_dormir_fim_semana", nullable = false)
    private String horarioDormirFimSemana;

    @Column(name = "horario_acordar_semana", nullable = false)
    private String horarioAcordarSemana;

    @Column(name = "horario_acordar_fim_semana", nullable = false)
    private String horarioAcordarFimSemana;

    @Column(name = "nivel_limpeza", nullable = false)
    private String nivelLimpeza;

    @Column(name = "nivel_organizacao", nullable = false)
    private String nivelOrganizacao;

    @Column(name = "tolerancia_ruido", nullable = false)
    private String toleranciaRuido;

    @Column(name = "frequencia_visitas", nullable = false)
    private String frequenciaVisitas;

    @Column(name = "frequencia_festas", nullable = false)
    private String frequenciaFestas;

    @Column(name = "tem_pet", nullable = false)
    private Boolean temPet;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em", nullable = false)
    private LocalDateTime atualizadoEm;

    protected Vaga() {
    }

    public Vaga(
            Usuario responsavel,
            String titulo,
            String descricao,
            String endereco,
            BigDecimal valorAluguel,
            Integer numeroMoradores,
            String regrasCasa,
            BigDecimal latitude,
            BigDecimal longitude,
            String horarioDormirSemana,
            String horarioDormirFimSemana,
            String horarioAcordarSemana,
            String horarioAcordarFimSemana,
            String nivelLimpeza,
            String nivelOrganizacao,
            String toleranciaRuido,
            String frequenciaVisitas,
            String frequenciaFestas,
            Boolean temPet) {
        this.responsavel = responsavel;
        this.titulo = titulo;
        this.descricao = descricao;
        this.endereco = endereco;
        this.valorAluguel = valorAluguel;
        this.numeroMoradores = numeroMoradores;
        this.regrasCasa = regrasCasa;
        this.latitude = latitude;
        this.longitude = longitude;
        this.horarioDormirSemana = horarioDormirSemana;
        this.horarioDormirFimSemana = horarioDormirFimSemana;
        this.horarioAcordarSemana = horarioAcordarSemana;
        this.horarioAcordarFimSemana = horarioAcordarFimSemana;
        this.nivelLimpeza = nivelLimpeza;
        this.nivelOrganizacao = nivelOrganizacao;
        this.toleranciaRuido = toleranciaRuido;
        this.frequenciaVisitas = frequenciaVisitas;
        this.frequenciaFestas = frequenciaFestas;
        this.temPet = temPet;
        this.status = Status.ATIVA;
    }

    @PrePersist
    void aoCriar() {
        LocalDateTime agora = LocalDateTime.now();
        criadoEm = agora;
        atualizadoEm = agora;
    }

    @PreUpdate
    void aoAtualizar() {
        atualizadoEm = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }
}
