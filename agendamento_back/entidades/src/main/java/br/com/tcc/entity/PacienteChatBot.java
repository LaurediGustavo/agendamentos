package br.com.tcc.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
@Table(name = "paciente_chatbot")
public class PacienteChatBot implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", length = 100)
    private String nome;

    @Column(name = "sobrenome", length = 100)
    private String sobrenome;

    @Column(name = "cpf", length = 14)
    private String cpf;

    @Column(name = "telefone", length = 15)
    private String telefone;

    @Column(name = "chat_id")
    private Long chatId;

}
