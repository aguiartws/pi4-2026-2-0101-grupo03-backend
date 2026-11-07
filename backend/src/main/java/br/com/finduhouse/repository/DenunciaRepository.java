package br.com.finduhouse.repository;

import br.com.finduhouse.entity.Denuncia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DenunciaRepository extends JpaRepository<Denuncia, Long> {

    boolean existsByVagaIdAndEstudanteId(Long vagaId, Long estudanteId);
}
