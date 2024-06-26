package br.com.tcc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@SpringBootApplication
@ComponentScan({"br.com.tcc.entidades",
                "br.com.tcc.service",
                "br.com.tcc.chatbot",
                "uteis",
                "br.com.tcc.scheduling",
                "br.com.tcc.security.securityConfig",
                "br.com.tcc.security.jwtConfig",
                "br.com.tcc.controller",
                "br.com.tcc.swagger",
                "br.com.tcc.email",
                "br.com.tcc.impl",
                "br.com.tcc.validation.agendamento.impl",
                "br.com.tcc.validation.agendamento.interfaces",
                "br.com.tcc.exceptions",
                "br.com.tcc.websocket"})
@EntityScan(basePackages = {"br.com.tcc.entity"})
@EnableJpaRepositories(basePackages = {"br.com.tcc.repository"})
@EnableScheduling
@EnableAsync
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

}
