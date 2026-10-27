package com.fts.gerenciamento.controle_clientes_backend.repository;

import com.fts.gerenciamento.controle_clientes_backend.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
