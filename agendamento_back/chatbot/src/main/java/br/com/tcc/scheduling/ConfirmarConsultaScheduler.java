package br.com.tcc.scheduling;

import br.com.tcc.chatbot.confirmacao.impl.ConfirmarConsultaMonitorDeMensagensBot;
import br.com.tcc.chatbot.confirmacao.interfaces.ConfirmarConsultaInterface;
import br.com.tcc.entity.Consulta;
import br.com.tcc.enumerador.StatusConsultaEnum;
import br.com.tcc.repository.ConsultaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Component
public class ConfirmarConsultaScheduler {

	@Autowired
	private ConsultaRepository consultaRepository;

	@Autowired
	private ConfirmarConsultaInterface confirmarConsultaBot;

	@Autowired
	private ConfirmarConsultaMonitorDeMensagensBot monitorChatBot;
	
	@Scheduled(cron = "*/5 * * * * *") //Segundos Minutos Horas DiaDoMês Mês DiaDaSemana
	public void run() {
		LocalDateTime dataInicio = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT).plusDays(1);
		LocalDateTime dataFim = LocalDateTime.of(LocalDate.now(), LocalTime.MAX).plusDays(1);

		Optional<List<Consulta>> consultaOptional = consultaRepository
				.consultarProximosAgendamentos(StatusConsultaEnum.AGUARDANDO,
												dataInicio,
												dataFim);

		consultaOptional.ifPresent(consultas -> {
			consultas.forEach(consulta -> {
				try {
					confirmarConsultaBot.iniciarConversa(consulta);
					monitorChatBot.adicionarMensagem(consulta);
				} catch (TelegramApiException e) {
					throw new RuntimeException(e);
				}
			});
		});
	}
	
}
