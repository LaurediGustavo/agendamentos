package br.com.tcc.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
@Table(name = "cancelar_agendamento_chatbot")
public class CancelarAgendamentoChatBot implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cpf", length = 14)
    private String cpf;

    @Column(name = "agendamento_id")
    private Long agendamentoId;

    @Column(name = "chat_id")
    private Long chatId;

    @Transient
    private Consulta consulta;
}
