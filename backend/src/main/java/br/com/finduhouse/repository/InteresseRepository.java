package br.com.finduhouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.finduhouse.entity.Interesse;

@Repository
public interface InteresseRepository extends JpaRepository<Interesse, Long> {
    boolean existsByEstudanteIdAndVagaId(long estudanteId, long vagaId);
    
    // ADICIONE ESTA LINHA:
    List<Interesse> findByEstudanteId(long estudanteId);

    void deleteByVagaId(Long vagaId);

    @Query("""
            SELECT i
            FROM Interesse i
            JOIN FETCH i.estudante
            JOIN FETCH i.vaga v
            WHERE v.responsavel.id = :responsavelId
            ORDER BY i.criadoEm DESC
            """)
    List<Interesse> findByResponsavelId(@Param("responsavelId") long responsavelId);
}