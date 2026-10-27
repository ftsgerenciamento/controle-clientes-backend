package com.fts.gerenciamento.controle_clientes_backend.service;

import java.util.List;

import com.fts.gerenciamento.controle_clientes_backend.api.dto.ClienteDto;
import com.fts.gerenciamento.controle_clientes_backend.model.Cliente;
import com.fts.gerenciamento.controle_clientes_backend.repository.ClienteRepository;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class ClienteService {

    private final ClienteRepository repository;

    ClienteService(ClienteRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<ClienteDto> listar() {
        return repository.findAll(Sort.by("projeto")).stream().map(Cliente::paraDto).toList();
    }

    @Transactional(readOnly = true)
    public ClienteDto buscar(Long id) {
        return encontrar(id).paraDto();
    }

    public ClienteDto criar(ClienteDto dto) {
        Cliente cliente = new Cliente();
        cliente.preencher(dto);
        return repository.saveAndFlush(cliente).paraDto();
    }

    public ClienteDto atualizar(Long id, ClienteDto dto) {
        Cliente cliente = encontrar(id);
        cliente.preencher(dto);
        // flush para os itens novos já voltarem com o id gerado pelo banco
        return repository.saveAndFlush(cliente).paraDto();
    }

    public void remover(Long id) {
        repository.delete(encontrar(id));
    }

    private Cliente encontrar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
    }
}
