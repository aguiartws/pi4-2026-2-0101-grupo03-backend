package br.com.finduhouse.repository;

import br.com.finduhouse.entity.VagaFoto;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VagaFotoRepository extends JpaRepository<VagaFoto, Long> {

    long countByVagaId(Long vagaId);

    List<VagaFoto> findByVagaIdOrderByOrdemAscIdAsc(Long vagaId);

    List<VagaFoto> findByVagaIdInOrderByOrdemAscIdAsc(Collection<Long> vagaIds);
}
