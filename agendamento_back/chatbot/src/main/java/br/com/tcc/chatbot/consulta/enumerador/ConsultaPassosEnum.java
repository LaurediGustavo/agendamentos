package br.com.tcc.chatbot.consulta.enumerador;

public enum ConsultaPassosEnum {

    PASSO_UM(1),
    PASSO_DOIS(2);

    private final Integer PASSO;

    ConsultaPassosEnum(Integer passo) {
        PASSO = passo;
    }

    public Integer getPASSO() {
        return PASSO;
    }

}
