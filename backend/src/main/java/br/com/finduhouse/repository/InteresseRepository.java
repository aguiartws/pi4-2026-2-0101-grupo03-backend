package br.com.finduhouse.repository;

import br.com.finduhouse.entity.Interesse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InteresseRepository extends JpaRepository<Interesse, Long> {

    boolean existsByEstudanteIdAndVagaId(Long estudanteId, Long vagaId);
}
