package br.com.tcc.repository;

import br.com.tcc.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    Optional<Paciente> findById(Long value);

    @Query("SELECT p FROM Paciente p " +
            "WHERE (p.nome LIKE %:nome% OR p.cpf LIKE %:cpf%) " +
            "AND (p.desabilitado IS NULL OR p.desabilitado = false) " +
            "ORDER BY p.nome")
    Optional<List<Paciente>> findByCpfNomeHabilitado(@Param("cpf") String cpf, @Param("nome") String nome);

    @Query("SELECT p FROM Paciente p " +
            "WHERE (p.nome LIKE %:nome% OR p.cpf LIKE %:cpf%) " +
            "AND (p.desabilitado = true) " +
            "ORDER BY p.nome")
    Optional<List<Paciente>> findByCpfNomeDesabilitado(@Param("cpf") String cpf, @Param("nome") String nome);

    @Query("SELECT p FROM Paciente p " +
            "WHERE p.nome LIKE %:nome% " +
            "AND (p.desabilitado IS NULL OR p.desabilitado = false) ")
    Optional<List<Paciente>> findByNome(@Param("nome") String nome);

    @Query("SELECT p FROM Paciente p " +
            "WHERE p.cpf LIKE :cpf " +
            "AND (p.desabilitado IS NULL OR p.desabilitado = false) " +
            "ORDER BY p.nome")
    Optional<Paciente> findByCpf(String cpf);

    Optional<Paciente> findByChatId(Long chatId);

}
