package com.fts.gerenciamento.controle_clientes_backend.model;

import com.fts.gerenciamento.controle_clientes_backend.api.dto.PagamentoDto;
import com.fts.gerenciamento.controle_clientes_backend.model.enums.StatusPagamento;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.math.BigDecimal;

@Entity
public class Pagamento implements ItemCliente<PagamentoDto> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String mes;
    private BigDecimal valor;
    @Enumerated(EnumType.STRING)
    private StatusPagamento status;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void preencher(PagamentoDto d) {
        mes = d.mes();
        valor = d.valor();
        status = d.status();
    }

    @Override
    public PagamentoDto paraDto() {
        return new PagamentoDto(id, mes, valor, status);
    }
}
