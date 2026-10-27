package com.fts.gerenciamento.controle_clientes_backend.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record DataImportanteDto(
        Long id,

        @NotBlank @Size(max = 200)
        String descricao,

        @NotNull LocalDate data) {
}
