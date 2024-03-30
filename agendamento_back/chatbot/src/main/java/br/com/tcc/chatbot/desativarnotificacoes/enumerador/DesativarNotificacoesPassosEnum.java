package br.com.tcc.chatbot.desativarnotificacoes.enumerador;

public enum DesativarNotificacoesPassosEnum {

    PASSO_UM(1),
    PASSO_DOIS(2);

    private final Integer PASSO;

    DesativarNotificacoesPassosEnum(Integer passo) {
        PASSO = passo;
    }

    public Integer getPASSO() {
        return PASSO;
    }

}
