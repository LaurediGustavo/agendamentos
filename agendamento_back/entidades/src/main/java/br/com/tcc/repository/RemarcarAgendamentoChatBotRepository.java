package br.com.tcc.repository;

import br.com.tcc.entity.RemarcarAgendamentoChatBot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RemarcarAgendamentoChatBotRepository extends JpaRepository<RemarcarAgendamentoChatBot, Long> {

    Optional<RemarcarAgendamentoChatBot> findTopByChatIdOrderByIdDesc(Long chatId);

    Optional<RemarcarAgendamentoChatBot> findByChatId(Long chatId);

}
