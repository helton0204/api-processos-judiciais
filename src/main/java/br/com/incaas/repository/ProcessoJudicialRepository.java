package br.com.incaas.repository;

import br.com.incaas.model.ProcessoJudicial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProcessoJudicialRepository extends JpaRepository<ProcessoJudicial, Long> {

    List<ProcessoJudicial> findByStatusAndComarca(String status, String comarca);
}
