package com.fts.gerenciamento.controle_clientes_backend.model;

import com.fts.gerenciamento.controle_clientes_backend.api.dto.DataImportanteDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;

@Entity
public class DataImportante implements ItemCliente<DataImportanteDto> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String descricao;
    private LocalDate data;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void preencher(DataImportanteDto d) {
        descricao = d.descricao();
        data = d.data();
    }

    @Override
    public DataImportanteDto paraDto() {
        return new DataImportanteDto(id, descricao, data);
    }
}
