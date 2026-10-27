package com.fts.gerenciamento.controle_clientes_backend.repository;

import java.util.Optional;

import com.fts.gerenciamento.controle_clientes_backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsuario(String usuario);
}
