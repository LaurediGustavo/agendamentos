package br.com.tcc.repository;

import br.com.tcc.entity.PacienteChatBot;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PacienteChatBotRepository extends JpaRepository<PacienteChatBot, Long> {

    @Query("SELECT m FROM PacienteChatBot m WHERE m.chatId = :chatId ORDER BY m.id desc limit 1")
    Optional<PacienteChatBot> findByChatId(@Param("chatId") Long chatId);

    @Transactional
    @Modifying
    @Query("DELETE FROM PacienteChatBot m WHERE m.chatId = :chatId")
    void deleteByChatId(@Param("chatId") Long chatId);

}
