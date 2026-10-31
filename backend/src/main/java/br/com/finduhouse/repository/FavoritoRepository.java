package br.com.finduhouse.repository;

import java.util.List;

import br.com.finduhouse.entity.Favorito;
import br.com.finduhouse.entity.FavoritoId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoritoRepository extends JpaRepository<Favorito, FavoritoId> {

    boolean existsByIdEstudanteIdAndIdVagaId(Long estudanteId, Long vagaId);

    List<Favorito> findByIdEstudanteId(Long estudanteId);
}
