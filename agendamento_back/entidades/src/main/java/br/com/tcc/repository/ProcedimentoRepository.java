package br.com.tcc.repository;

import br.com.tcc.entity.Procedimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProcedimentoRepository extends JpaRepository<Procedimento, Long> {

}
