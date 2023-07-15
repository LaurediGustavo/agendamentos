package br.com.tcc.scheduling;

import br.com.tcc.chatbot.confirmacao.impl.MonitorDeMensagensChatBot;
import br.com.tcc.chatbot.confirmacao.interfaces.ConfirmarConsultaInterface;
import br.com.tcc.entity.Paciente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class ConfirmarConsultaScheduler {
	
	@Autowired
	private ConfirmarConsultaInterface confirmarConsultaBot;

	@Autowired
	private MonitorDeMensagensChatBot monitorChatBot;
	
	int i = 0;
	
	@Scheduled(cron = "*/5 * * * * *") //Segundos Minutos Horas DiaDoMês Mês DiaDaSemana
	public void run() {
	/*	try {
			if(i == 0) {
				Paciente p = new Paciente();
				p.setChatId(6292182354L);
				
				confirmarConsultaBot.iniciarConversa(p);
				monitorChatBot.adicionarMensagem(p);
			}
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
		System.out.println("teste");
		i = 1;*/
	}
	
}
