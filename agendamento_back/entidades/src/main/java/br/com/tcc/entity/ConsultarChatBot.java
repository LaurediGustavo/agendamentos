package br.com.tcc.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
@Table(name = "consultar_chatbot")
public class ConsultarChatBot implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cpf", length = 14)
    private String cpf;

    @Column(name = "chat_id")
    private Long chatId;

}
