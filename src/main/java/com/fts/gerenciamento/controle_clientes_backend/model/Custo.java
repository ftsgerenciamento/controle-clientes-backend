package com.fts.gerenciamento.controle_clientes_backend.model;

import com.fts.gerenciamento.controle_clientes_backend.api.dto.CustoDto;
import com.fts.gerenciamento.controle_clientes_backend.model.enums.Recorrencia;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.math.BigDecimal;

@Entity
public class Custo implements ItemCliente<CustoDto> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String descricao;
    private BigDecimal valor;
    @Enumerated(EnumType.STRING)
    private Recorrencia recorrencia;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void preencher(CustoDto d) {
        descricao = d.descricao();
        valor = d.valor();
        recorrencia = d.recorrencia();
    }

    @Override
    public CustoDto paraDto() {
        return new CustoDto(id, descricao, valor, recorrencia);
    }
}
