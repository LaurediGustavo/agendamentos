package br.com.tcc.repository;

import br.com.tcc.entity.ConsultaEstendida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultaEstendidaRepository extends JpaRepository<ConsultaEstendida, Long> {
}
