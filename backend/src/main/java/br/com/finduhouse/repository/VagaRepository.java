package br.com.finduhouse.repository;

import br.com.finduhouse.entity.Vaga;
import java.util.List;
import java.math.BigDecimal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VagaRepository extends JpaRepository<Vaga, Long> {

    List<Vaga> findByResponsavelId(Long responsavelId);

    List<Vaga> findByStatusAndValorAluguelLessThanEqual(
            Vaga.Status status,
            BigDecimal valorAluguel);

    List<Vaga> findByStatus(Vaga.Status status);
}
