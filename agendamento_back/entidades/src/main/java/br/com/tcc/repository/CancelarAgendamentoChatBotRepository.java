package br.com.tcc.repository;

import br.com.tcc.entity.CancelarAgendamentoChatBot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CancelarAgendamentoChatBotRepository extends JpaRepository<CancelarAgendamentoChatBot, Long> {

    Optional<CancelarAgendamentoChatBot> findTopByChatIdOrderByIdDesc(Long chatId);

}
