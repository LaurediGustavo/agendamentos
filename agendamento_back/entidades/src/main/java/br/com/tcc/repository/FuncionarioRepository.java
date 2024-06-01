package br.com.tcc.repository;

import br.com.tcc.entity.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

    @Query("SELECT f FROM Funcionario f " +
            "JOIN tipoFuncionario t " +
            "WHERE f.nome LIKE %:nome% " +
            "AND (f.desabilitado IS NULL OR f.desabilitado = false) " +
            "AND t.nome <> 'DOUTOR' " +
            "ORDER BY f.nome ")
    Optional<List<Funcionario>> findByNomeContainingAndHabilitado(String nome);

    @Query("SELECT f FROM Funcionario f " +
            "JOIN tipoFuncionario t " +
            "WHERE f.nome LIKE %:nome% " +
            "AND (f.desabilitado = true) " +
            "AND t.nome <> 'DOUTOR' " +
            "ORDER BY f.nome ")
    Optional<List<Funcionario>> findByNomeContainingAndDesabilitado(String nome);

    boolean existsByCpf(String cpf);

    boolean existsByEmail(String email);

    Optional<Funcionario> findByEmail(String email);

}
