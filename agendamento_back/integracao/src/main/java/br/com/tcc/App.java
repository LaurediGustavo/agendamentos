package br.com.tcc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan({"br.com.tcc.entidades",
                "br.com.tcc.service",
                "br.com.tcc.chatbot",
                "uteis",
                "br.com.tcc.scheduling",
                "br.com.tcc.security.securityConfig",
                "br.com.tcc.security.jwtConfig",
                "br.com.tcc.controller",
                "br.com.tcc.impl",
                "br.com.tcc.validation.agendamento.impl",
                "br.com.tcc.validation.agendamento.interfaces",
                "br.com.tcc.exceptions"})
@EntityScan(basePackages = {"br.com.tcc.entity"})
@EnableJpaRepositories(basePackages = {"br.com.tcc.repository"})
@EnableScheduling
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

}
