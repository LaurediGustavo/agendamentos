package br.com.tcc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan({"br.com.tcc.entidades", "br.com.tcc.service", "br.com.tcc.chatbot", "br.com.tcc.scheduling"})
@EntityScan(basePackages = {"br.com.tcc.entity"})
@EnableJpaRepositories(basePackages = {"br.com.tcc.repository"})
@EnableScheduling
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

}
