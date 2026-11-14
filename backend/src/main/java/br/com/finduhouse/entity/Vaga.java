package br.com.finduhouse.entity;

import java.math.BigDecimal;
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

    @Column(name = "aceita_barulho_depois_das_22", nullable = false)
    private Boolean aceitaBarulhoDepoisDas22;
    @Column(nullable = false)
    private Boolean fuma;
    @Column(name = "aceita_animais", nullable = false)
    private Boolean aceitaAnimais;
    @Column(name = "gosta_receber_visitas", nullable = false)
    private Boolean gostaReceberVisitas;
    @Column(name = "gosta_cozinhar", nullable = false)
    private Boolean gostaCozinhar;
    @Column(name = "estuda_em_casa", nullable = false)
    private Boolean estudaEmCasa;
    @Column(name = "aceita_dividir_tarefas", nullable = false)
    private Boolean aceitaDividirTarefas;
    @Column(name = "aceita_dividir_quarto", nullable = false)
    private Boolean aceitaDividirQuarto;
    @Column(name = "aceita_festas", nullable = false)
    private Boolean aceitaFestas;
    @Column(nullable = false)
    private Boolean comunicativo;

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
            Boolean aceitaBarulhoDepoisDas22,
            Boolean fuma,
            Boolean aceitaAnimais,
            Boolean gostaReceberVisitas,
            Boolean gostaCozinhar,
            Boolean estudaEmCasa,
            Boolean aceitaDividirTarefas,
            Boolean aceitaDividirQuarto,
            Boolean aceitaFestas,
            Boolean comunicativo) {
        this.responsavel = responsavel;
        this.titulo = titulo;
        this.descricao = descricao;
        this.endereco = endereco;
        this.valorAluguel = valorAluguel;
        this.numeroMoradores = numeroMoradores;
        this.regrasCasa = regrasCasa;
        this.latitude = latitude;
        this.longitude = longitude;
        this.aceitaBarulhoDepoisDas22 = aceitaBarulhoDepoisDas22;
        this.fuma = fuma;
        this.aceitaAnimais = aceitaAnimais;
        this.gostaReceberVisitas = gostaReceberVisitas;
        this.gostaCozinhar = gostaCozinhar;
        this.estudaEmCasa = estudaEmCasa;
        this.aceitaDividirTarefas = aceitaDividirTarefas;
        this.aceitaDividirQuarto = aceitaDividirQuarto;
        this.aceitaFestas = aceitaFestas;
        this.comunicativo = comunicativo;
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

    public Usuario getResponsavel() {
        return responsavel;
    }

    public BigDecimal getValorAluguel() {
        return valorAluguel;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getDescricao() {
        return descricao;
    }

    public Integer getNumeroMoradores() {
        return numeroMoradores;
    }

    public String getRegrasCasa() {
        return regrasCasa;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public Boolean getAceitaBarulhoDepoisDas22() {
        return aceitaBarulhoDepoisDas22;
    }

    public Boolean getFuma() {
        return fuma;
    }

    public Boolean getAceitaAnimais() {
        return aceitaAnimais;
    }

    public Boolean getGostaReceberVisitas() {
        return gostaReceberVisitas;
    }

    public Boolean getGostaCozinhar() {
        return gostaCozinhar;
    }

    public Boolean getEstudaEmCasa() {
        return estudaEmCasa;
    }

    public Boolean getAceitaDividirTarefas() {
        return aceitaDividirTarefas;
    }

    public Boolean getAceitaDividirQuarto() {
        return aceitaDividirQuarto;
    }

    public Boolean getAceitaFestas() {
        return aceitaFestas;
    }

    public Boolean getComunicativo() {
        return comunicativo;
    }

    public void marcarEmAnalise() {
        if (status == Status.ATIVA) {
            status = Status.EM_ANALISE;
        }
    }

    public void atualizarDados(
            String titulo,
            String descricao,
            String endereco,
            BigDecimal valorAluguel,
            Integer numeroMoradores,
            String regrasCasa,
            BigDecimal latitude,
            BigDecimal longitude,
            Status status,
            Boolean aceitaBarulhoDepoisDas22,
            Boolean fuma,
            Boolean aceitaAnimais,
            Boolean gostaReceberVisitas,
            Boolean gostaCozinhar,
            Boolean estudaEmCasa,
            Boolean aceitaDividirTarefas,
            Boolean aceitaDividirQuarto,
            Boolean aceitaFestas,
            Boolean comunicativo) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.endereco = endereco;
        this.valorAluguel = valorAluguel;
        this.numeroMoradores = numeroMoradores;
        this.regrasCasa = regrasCasa;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
        this.aceitaBarulhoDepoisDas22 = aceitaBarulhoDepoisDas22;
        this.fuma = fuma;
        this.aceitaAnimais = aceitaAnimais;
        this.gostaReceberVisitas = gostaReceberVisitas;
        this.gostaCozinhar = gostaCozinhar;
        this.estudaEmCasa = estudaEmCasa;
        this.aceitaDividirTarefas = aceitaDividirTarefas;
        this.aceitaDividirQuarto = aceitaDividirQuarto;
        this.aceitaFestas = aceitaFestas;
        this.comunicativo = comunicativo;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public void atualizarValorAluguel(BigDecimal novoValor) {
        valorAluguel = novoValor;
    }
}
