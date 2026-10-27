package com.fts.gerenciamento.controle_clientes_backend.api.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(@NotBlank String usuario, @NotBlank String senha) {
}
