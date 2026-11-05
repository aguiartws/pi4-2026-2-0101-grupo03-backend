package br.com.finduhouse.entity;

import java.time.LocalDateTime;
import java.util.Locale;

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
        horarioDormirSemana = dados.horarioDormirSemana();
        horarioDormirFimSemana = dados.horarioDormirFimSemana();
        horarioAcordarSemana = dados.horarioAcordarSemana();
        horarioAcordarFimSemana = dados.horarioAcordarFimSemana();
        nivelLimpeza = dados.nivelLimpeza();
        nivelOrganizacao = dados.nivelOrganizacao();
        toleranciaRuido = dados.toleranciaRuido();
        frequenciaVisitas = dados.frequenciaVisitas();
        frequenciaFestas = dados.frequenciaFestas();
        temPet = dados.temPet();
    }

    @PrePersist
    @PreUpdate
    void atualizarTimestamp() {
        atualizadoEm = LocalDateTime.now();
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public String getHorarioDormirSemana() {
        return horarioDormirSemana;
    }

    public String getHorarioDormirFimSemana() {
        return horarioDormirFimSemana;
    }

    public String getHorarioAcordarSemana() {
        return horarioAcordarSemana;
    }

    public String getHorarioAcordarFimSemana() {
        return horarioAcordarFimSemana;
    }

    public String getNivelLimpeza() {
        return nivelLimpeza;
    }

    public String getNivelOrganizacao() {
        return nivelOrganizacao;
    }

    public String getToleranciaRuido() {
        return toleranciaRuido;
    }

    public String getFrequenciaVisitas() {
        return frequenciaVisitas;
    }

    public String getFrequenciaFestas() {
        return frequenciaFestas;
    }

    public Boolean getTemPet() {
        return temPet;
    }

    public record Dados(
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
    }
}
