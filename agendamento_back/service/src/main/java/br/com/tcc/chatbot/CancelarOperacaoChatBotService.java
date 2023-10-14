package br.com.tcc.chatbot;

import br.com.tcc.entity.Consulta;
import br.com.tcc.entity.Procedimento;
import br.com.tcc.repository.ConsultaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uteis.DataUteis;
import uteis.Uteis;

import java.util.List;
import java.util.stream.IntStream;

@Service
public class CancelarOperacaoChatBotService {

    @Autowired
    private ConsultaRepository consultaRepository;

    public String consultasDisponiveis(String cpf) {
        StringBuilder consultas = new StringBuilder();
        consultas.append("Lista de consultas agendadas: \n\n");

        List<Consulta> consultaList = consultaRepository
                .findConsultasByStatusAndCpfAndDataHoraInicio(cpf);

        if(!consultaList.isEmpty()) {
            consultas.append(IntStream.range(0, consultaList.size())
                    .mapToObj(index -> {
                        Consulta con = consultaList.get(index);
                        return new StringBuilder()
                                .append(index + 1)
                                .append(" - ")
                                .append(DataUteis.getLocalDateTime_ddMMaaaaHHMM(con.getDataHoraInicio()))
                                .append("\n")
                                .append("\t\t\t\t\t\t")
                                .append("Procedimento: \n")
                                .append(getProcedimentos(con.getProcedimentos()))
                                .append("\n")
                                .append("\t\t\t\t\t\t")
                                .append("Valor: ")
                                .append(Uteis.formatarMoedaParaReal(con.getValorTotal()))
                                .append("\n\n");
                    })
                    .collect(
                            StringBuilder::new,
                            StringBuilder::append,
                            StringBuilder::append
                    ).toString());

            consultas.append("\n")
                    .append("Informe o número da consulta que você deseja concelar.");
        }
        else {
            consultas.append("Você não possui consultas agendadas");
        }

        return consultas.toString();
    }

    private String getProcedimentos(List<Procedimento> procedimentos) {
        return procedimentos.stream()
                .map(procedimento -> new StringBuilder()
                        .append("\t\t\t\t\t\t\t\t\t" + procedimento.getTratamento() + "\n"))
                .collect(
                        StringBuilder::new,
                        StringBuilder::append,
                        StringBuilder::append
                ).toString();
    }

}
