package br.com.incaas.repository;

import br.com.incaas.model.Audiencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AudienciaRepository extends JpaRepository<Audiencia, Long> {
}
