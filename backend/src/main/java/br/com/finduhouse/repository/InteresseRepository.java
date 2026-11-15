package br.com.finduhouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.finduhouse.entity.Interesse;

@Repository
public interface InteresseRepository extends JpaRepository<Interesse, Long> {
    boolean existsByEstudanteIdAndVagaId(long estudanteId, long vagaId);
    
    // ADICIONE ESTA LINHA:
    List<Interesse> findByEstudanteId(long estudanteId);
}