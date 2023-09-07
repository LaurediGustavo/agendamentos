package br.com.tcc.repository;

import br.com.tcc.entity.PacienteChatBot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PacienteChatBotRepository extends JpaRepository<PacienteChatBot, Long> {

    Optional<PacienteChatBot> findByChatId(Long chatId);

}
