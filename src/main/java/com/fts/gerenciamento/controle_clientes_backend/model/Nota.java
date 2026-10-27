package com.fts.gerenciamento.controle_clientes_backend.model;

import com.fts.gerenciamento.controle_clientes_backend.api.dto.NotaDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;

@Entity
public class Nota implements ItemCliente<NotaDto> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate data;
    private String texto;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void preencher(NotaDto d) {
        data = d.data();
        texto = d.texto();
    }

    @Override
    public NotaDto paraDto() {
        return new NotaDto(id, data, texto);
    }
}
