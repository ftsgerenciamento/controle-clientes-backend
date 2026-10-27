package com.fts.gerenciamento.controle_clientes_backend.config;

import com.fts.gerenciamento.controle_clientes_backend.model.Usuario;
import com.fts.gerenciamento.controle_clientes_backend.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
class AdminInicial implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(AdminInicial.class);

    private final UsuarioRepository repository;
    private final PasswordEncoder encoder;
    private final String usuario;
    private final String senha;

    AdminInicial(UsuarioRepository repository, PasswordEncoder encoder,
                 @Value("${app.admin.usuario}") String usuario, @Value("${app.admin.senha}") String senha) {
        this.repository = repository;
        this.encoder = encoder;
        this.usuario = usuario;
        this.senha = senha;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (repository.count() > 0) return;
        repository.save(new Usuario(usuario, encoder.encode(senha)));
        log.info("Usuário inicial '{}' criado", usuario);
        if ("admin".equals(senha)) {
            log.warn("Usando a senha padrão 'admin' — defina ADMIN_SENHA antes de usar em produção");
        }
    }
}
