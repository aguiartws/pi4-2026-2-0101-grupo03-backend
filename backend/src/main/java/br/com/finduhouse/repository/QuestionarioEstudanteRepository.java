package br.com.finduhouse.repository;

import java.util.Optional;

import br.com.finduhouse.entity.QuestionarioEstudante;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionarioEstudanteRepository
        extends JpaRepository<QuestionarioEstudante, Long> {

    Optional<QuestionarioEstudante> findByEstudanteId(Long estudanteId);
}
