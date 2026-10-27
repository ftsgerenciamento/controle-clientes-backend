package com.fts.gerenciamento.controle_clientes_backend.model.enums;

// "Atrasado" não é guardado: o frontend calcula a partir de PENDENTE + dia de vencimento.
public enum StatusPagamento {
    PENDENTE,
    PAGO
}
