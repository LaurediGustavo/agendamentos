package br.com.tcc.repository;

import java.util.List;
import java.util.Optional;

import br.com.tcc.entity.MonitorDeChatBot;
import br.com.tcc.enumerador.StatusDaMensagemChatBotEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface MonitorDeChatBotRepository extends JpaRepository<MonitorDeChatBot, Long> {

    Optional<MonitorDeChatBot> findByChatIdAndStatusDaMensagemChatBotEnum(Long chatId, StatusDaMensagemChatBotEnum statusDaMensagemChatBotEnum);

    @Query("SELECT m FROM MonitorDeChatBot m " +
            "WHERE TIMESTAMPDIFF(MINUTE, m.dataDaMensagem, CURRENT_TIMESTAMP) > 5 " +
            "AND m.statusDaMensagemChatBotEnum = 'AGUARDANDO'")
    Optional<List<MonitorDeChatBot>> findMensagensAguardando();

}
