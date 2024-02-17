package br.com.tcc.repository;

import br.com.tcc.entity.CancelarAgendamentoChatBot;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CancelarAgendamentoChatBotRepository extends JpaRepository<CancelarAgendamentoChatBot, Long> {

    Optional<CancelarAgendamentoChatBot> findTopByChatIdOrderByIdDesc(Long chatId);

    @Transactional
    @Modifying
    @Query("DELETE FROM CancelarAgendamentoChatBot m WHERE m.chatId = :chatId")
    void deleteByChatId(@Param("chatId") Long chatId);

}
