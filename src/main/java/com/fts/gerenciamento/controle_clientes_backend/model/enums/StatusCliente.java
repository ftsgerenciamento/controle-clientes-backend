package com.fts.gerenciamento.controle_clientes_backend.model.enums;

// Os "ativos" (que entram no MRR e recebem cobrança mensal) são EM_DESENVOLVIMENTO e EM_MANUTENCAO — regra calculada no frontend.
public enum StatusCliente {
    EM_NEGOCIACAO,
    EM_DESENVOLVIMENTO,
    EM_MANUTENCAO,
    ENTREGUE_SEM_MANUTENCAO,
    PAUSADO,
    ENCERRADO
}
