package com.fts.gerenciamento.controle_clientes_backend.api.dto;

import com.fts.gerenciamento.controle_clientes_backend.model.enums.Recorrencia;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record CustoDto(
        Long id,
        @NotBlank @Size(max = 200)
        String descricao,

        @NotNull @PositiveOrZero
        BigDecimal valor,

        @NotNull
        Recorrencia recorrencia) {
}
