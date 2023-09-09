package br.com.tcc.entity;

import br.com.tcc.enumerador.StatusDaMensagemChatBotEnum;
import br.com.tcc.enumerador.TipoChatBotEnum;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "monitordechatbot")
public class MonitorDeChatBot implements Serializable {

	private static final long serialVersionUID = 7868953342369139642L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "datadamensagem", nullable = false)
	private LocalDateTime dataDaMensagem;
	
	@Column(name = "chatid", nullable = false)
	private Long chatId;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "tipo", nullable = false)
	private TipoChatBotEnum tipoChatBotEnum;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "statusdamensagem", nullable = false)
	private StatusDaMensagemChatBotEnum statusDaMensagemChatBotEnum;

	@Column(name = "passo", nullable = false)
	private Integer passo;

}
