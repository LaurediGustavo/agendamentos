package br.com.tcc.chatbot.ativarnotificacao.enumerador;

public enum AtivarNotificacoesPassosEnum {

    PASSO_UM(1),
    PASSO_DOIS(2);

    private final Integer PASSO;

    AtivarNotificacoesPassosEnum(Integer passo) {
        PASSO = passo;
    }

    public Integer getPASSO() {
        return PASSO;
    }

}
