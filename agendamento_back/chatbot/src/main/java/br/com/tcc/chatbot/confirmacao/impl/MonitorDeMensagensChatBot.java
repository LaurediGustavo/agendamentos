package br.com.tcc.chatbot.confirmacao.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import br.com.tcc.entity.Consulta;
import br.com.tcc.entity.MonitorDeChatBot;
import br.com.tcc.entity.Paciente;
import br.com.tcc.enumerador.StatusConsultaEnum;
import br.com.tcc.enumerador.StatusDaMensagemChatBotEnum;
import br.com.tcc.enumerador.TipoChatBotEnum;
import br.com.tcc.repository.ConsultaRepository;
import br.com.tcc.repository.MonitorDeChatBotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("MonitorDeMensagensChatBot")
public class MonitorDeMensagensChatBot {

	@Autowired
	private MonitorDeChatBotRepository monitorDeChatBotRepository;

	@Autowired
	private ConsultaRepository consultaRepository;

	public void adicionarMensagem(Consulta consulta) {
		iniciarMensagem(consulta.getPaciente());
		atualizarStatusConsulta(consulta);
	}

	private void atualizarStatusConsulta(Consulta consulta) {
		consulta.setStatus(StatusConsultaEnum.ENVIADO);
		consultaRepository.save(consulta);
	}

	void iniciarMensagem(Paciente paciente) {
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
