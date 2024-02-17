package br.com.tcc.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "remarcar_agendamento_chatbot")
public class RemarcarAgendamentoChatBot implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "cpf", length = 14)
    private String cpf;

    @Column(name = "agendamento_id")
    private Long agendamentoId;

    @Column(name = "horario")
    private LocalDateTime horario;

    @Transient
    private Consulta consulta;

}