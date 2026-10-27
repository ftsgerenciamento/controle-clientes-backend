package com.fts.gerenciamento.controle_clientes_backend.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record NotaDto(
        Long id,

        LocalDate data,

        String texto) {
}
