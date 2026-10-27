package com.fts.gerenciamento.controle_clientes_backend.model;

interface ItemCliente<D> {

    Long getId();

    void preencher(D dto);

    D paraDto();
}
