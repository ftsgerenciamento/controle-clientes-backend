package com.fts.gerenciamento.controle_clientes_backend.model.enums;

// No custo mensal: ANUAL é dividido por 12 e UNICO não entra (cálculo no frontend).
public enum Recorrencia {
    MENSAL,
    ANUAL,
    UNICO
}
