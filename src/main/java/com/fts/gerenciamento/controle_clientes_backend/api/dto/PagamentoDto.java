package com.fts.gerenciamento.controle_clientes_backend.api.dto;

import com.fts.gerenciamento.controle_clientes_backend.model.enums.StatusPagamento;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

// Sem id = item novo; o id é gerado pelo banco.
public record PagamentoDto(
        Long id,
        @NotNull @Pattern(regexp = "\\d{4}-\\d{2}", message = "deve estar no formato AAAA-MM")
        String mes,

        @NotNull @PositiveOrZero
        BigDecimal valor,

        @NotNull
        StatusPagamento status) {
}
