package br.com.tcc.repository;

import br.com.tcc.entity.TipoFuncionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoFuncionarioRepository extends JpaRepository<TipoFuncionario, Long> {
}
