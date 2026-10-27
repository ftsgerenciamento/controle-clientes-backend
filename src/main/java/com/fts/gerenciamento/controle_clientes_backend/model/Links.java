package com.fts.gerenciamento.controle_clientes_backend.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Size;

@Embeddable
public record Links(
        @Size(max = 500) String site,
        @Size(max = 500) String repositorio,
        @Size(max = 500) String hospedagem,
        @Size(max = 500) String arquivos) {

    public static final Links VAZIO = new Links(null, null, null, null);
}
