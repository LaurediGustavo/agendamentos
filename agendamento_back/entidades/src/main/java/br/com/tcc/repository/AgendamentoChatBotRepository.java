package br.com.tcc.repository;

import br.com.tcc.entity.AgendamentoChatBot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AgendamentoChatBotRepository extends JpaRepository<AgendamentoChatBot, Long> {

    Optional<AgendamentoChatBot> findByChatId(Long chatId);

}
