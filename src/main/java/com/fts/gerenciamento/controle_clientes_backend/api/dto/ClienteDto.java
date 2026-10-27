package com.fts.gerenciamento.controle_clientes_backend.api.dto;

import com.fts.gerenciamento.controle_clientes_backend.model.Links;
import com.fts.gerenciamento.controle_clientes_backend.model.enums.FormaPagamento;
import com.fts.gerenciamento.controle_clientes_backend.model.enums.StatusCliente;
import com.fts.gerenciamento.controle_clientes_backend.model.enums.TipoServico;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record ClienteDto(
        Long id,

        @NotBlank @Size(max = 200)
        String projeto,

        @Size(max = 200)
        String cliente,

        @Size(max = 200)
        String contatoNome,

        @Size(max = 50)
        String telefone,

        @Size(max = 200)
        String email,

        String descricao,

        @NotNull
        TipoServico tipoServico,

        @NotNull
        StatusCliente status,

        LocalDate dataInicio,

        LocalDate dataFim,

        @Valid
        Links links,

        @PositiveOrZero
        BigDecimal valorProjeto,

        @PositiveOrZero
        BigDecimal mensalidade,

        @Min(1) @Max(31)
        Integer diaVencimento,

        FormaPagamento formaPagamento,

        @Valid @NotNull
        List<PagamentoDto> pagamentos,

        @Valid @NotNull
        List<CustoDto> custos,

        @Valid @NotNull
        List<DataImportanteDto> datas,

        @Valid @NotNull
        List<NotaDto> notas,

        String proximaAcao,

        String escopo) {

    public ClienteDto {
        links = links == null ? Links.VAZIO : links;
        pagamentos = pagamentos == null ? List.of() : pagamentos;
        custos = custos == null ? List.of() : custos;
        datas = datas == null ? List.of() : datas;
        notas = notas == null ? List.of() : notas;
    }
}
