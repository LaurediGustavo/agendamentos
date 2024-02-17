package br.com.tcc.repository;

import br.com.tcc.entity.RemarcarAgendamentoChatBot;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RemarcarAgendamentoChatBotRepository extends JpaRepository<RemarcarAgendamentoChatBot, Long> {

    Optional<RemarcarAgendamentoChatBot> findTopByChatIdOrderByIdDesc(Long chatId);

    Optional<RemarcarAgendamentoChatBot> findByChatId(Long chatId);

    @Transactional
    @Modifying
    @Query("DELETE FROM RemarcarAgendamentoChatBot m WHERE m.chatId = :chatId")
    void deleteByChatId(@Param("chatId") Long chatId);

}
