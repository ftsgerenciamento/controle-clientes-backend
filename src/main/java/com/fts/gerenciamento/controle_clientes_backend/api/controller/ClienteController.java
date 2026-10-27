package com.fts.gerenciamento.controle_clientes_backend.api.controller;

import com.fts.gerenciamento.controle_clientes_backend.api.dto.ClienteDto;
import com.fts.gerenciamento.controle_clientes_backend.service.ClienteService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clientes")
class ClienteController {

    private final ClienteService service;

    ClienteController(ClienteService service) {
        this.service = service;
    }

    @GetMapping
    List<ClienteDto> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    ClienteDto buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ClienteDto criar(@Valid @RequestBody ClienteDto dto) {
        return service.criar(dto);
    }

    @PutMapping("/{id}")
    ClienteDto atualizar(@PathVariable Long id, @Valid @RequestBody ClienteDto dto) {
        return service.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void remover(@PathVariable Long id) {
        service.remover(id);
    }
}
