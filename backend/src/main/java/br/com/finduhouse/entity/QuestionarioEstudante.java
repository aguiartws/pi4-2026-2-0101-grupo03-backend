package br.com.finduhouse.entity;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
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
@Table(name = "questionario_estudante")
public class QuestionarioEstudante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "estudante_id", nullable = false, unique = true)
    private Estudante estudante;

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

    @Column(name = "atualizado_em", nullable = false)
    private LocalDateTime atualizadoEm;

    protected QuestionarioEstudante() {
    }

    public QuestionarioEstudante(Estudante estudante, Dados dados) {
        this.estudante = estudante;
        aplicar(dados);
    }

    public void atualizar(Dados dados) {
        aplicar(dados);
    }

    private void aplicar(Dados dados) {
        aceitaBarulhoDepoisDas22 = dados.aceitaBarulhoDepoisDas22();
        fuma = dados.fuma();
        aceitaAnimais = dados.aceitaAnimais();
        gostaReceberVisitas = dados.gostaReceberVisitas();
        gostaCozinhar = dados.gostaCozinhar();
        estudaEmCasa = dados.estudaEmCasa();
        aceitaDividirTarefas = dados.aceitaDividirTarefas();
        aceitaDividirQuarto = dados.aceitaDividirQuarto();
        aceitaFestas = dados.aceitaFestas();
        comunicativo = dados.comunicativo();
    }

    @PrePersist
    @PreUpdate
    void atualizarTimestamp() {
        atualizadoEm = LocalDateTime.now();
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public Boolean getAceitaBarulhoDepoisDas22() { return aceitaBarulhoDepoisDas22; }
    public Boolean getFuma() { return fuma; }
    public Boolean getAceitaAnimais() { return aceitaAnimais; }
    public Boolean getGostaReceberVisitas() { return gostaReceberVisitas; }
    public Boolean getGostaCozinhar() { return gostaCozinhar; }
    public Boolean getEstudaEmCasa() { return estudaEmCasa; }
    public Boolean getAceitaDividirTarefas() { return aceitaDividirTarefas; }
    public Boolean getAceitaDividirQuarto() { return aceitaDividirQuarto; }
    public Boolean getAceitaFestas() { return aceitaFestas; }
    public Boolean getComunicativo() { return comunicativo; }

    public record Dados(
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
    }
}
