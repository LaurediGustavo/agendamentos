package br.com.tcc.chatbot.confirmacao.impl;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

import br.com.tcc.entity.MonitorDeChatBot;
import br.com.tcc.entity.Paciente;
import br.com.tcc.enumerador.StatusDaMensagemChatBotEnum;
import br.com.tcc.enumerador.TipoChatBotEnum;
import br.com.tcc.repository.MonitorDeChatBotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("MonitorDeMensagensChatBot")
public class MonitorDeMensagensChatBot {

	@Autowired
	private MonitorDeChatBotRepository monitorDeChatBotRepository;
	
	public void adicionarMensagem(Paciente paciente) {
		MonitorDeChatBot monitor = new MonitorDeChatBot();
		monitor.setDataDaMensagem(LocalDateTime.now());
		monitor.setTipoChatBotEnum(TipoChatBotEnum.CONFIRMACAO);
		monitor.setStatusDaMensagemChatBotEnum(StatusDaMensagemChatBotEnum.AGUARDANDO);
		monitor.setChatId(paciente.getChatId());
		
		monitorDeChatBotRepository.save(monitor);
	}
	
	public Optional<MonitorDeChatBot> consultarPorChatIdAndStatus(Long chatId, StatusDaMensagemChatBotEnum status) {
		return monitorDeChatBotRepository.findByChatIdAndStatusDaMensagemChatBotEnum(chatId, status);
	}
	
}
