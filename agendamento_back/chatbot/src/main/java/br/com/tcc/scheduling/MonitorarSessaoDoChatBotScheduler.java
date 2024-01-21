package br.com.tcc.scheduling;

import br.com.tcc.chatbot.sessao.EncerrarSessao;
import br.com.tcc.chatbot.sessao.EncerrarSessaoBot;
import br.com.tcc.entity.MonitorDeChatBot;
import br.com.tcc.repository.ConsultaRepository;
import br.com.tcc.repository.MonitorDeChatBotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Optional;

@Component
public class MonitorarSessaoDoChatBotScheduler {

	@Autowired
	private MonitorDeChatBotRepository monitorDeChatBotRepository;

	@Autowired
	private EncerrarSessao encerrarSessao;

	@Autowired
	private EncerrarSessaoBot encerrarSessaoBot;

	@Scheduled(cron = "*/5 * * * * *") //Segundos Minutos Horas DiaDoMês Mês DiaDaSemana
	public void run() {
		Optional<List<MonitorDeChatBot>> monitores = monitorDeChatBotRepository.findMensagensAguardando();

		monitores.ifPresent(monitorDeChatBots -> {
			monitorDeChatBots.forEach(monitor -> {
				try {
					encerrarSessao.encerrarSessao(monitor);
					encerrarSessaoBot.enviarMensagemDeEncerramento(monitor);
				} catch (TelegramApiException e) {
					throw new RuntimeException(e);
				}
			});
		});
	}
	
}
