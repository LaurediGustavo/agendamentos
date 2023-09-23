package br.com.tcc.chatbot.agendamento.enumerador;

public enum AgendamentoPassosEnum {

    PASSO_UM(1),
    PASSO_DOIS(2),
    PASSO_TRES(3),
    PASSO_QUATRO(4),
    PASSO_CINCO(5),
    PASSO_SEIS(6);

    private final Integer PASSO;

    AgendamentoPassosEnum(Integer passo) {
        PASSO = passo;
    }

    public Integer getPASSO() {
        return PASSO;
    }
}
