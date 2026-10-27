package com.fts.gerenciamento.controle_clientes_backend.api.controller;

import com.fts.gerenciamento.controle_clientes_backend.api.dto.LoginRequest;
import com.fts.gerenciamento.controle_clientes_backend.api.dto.LoginResponse;
import jakarta.validation.Valid;
import java.time.Duration;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;
    private final Duration validade;

    AuthController(AuthenticationManager authenticationManager, JwtEncoder jwtEncoder,
                   @Value("${app.jwt.validade}") Duration validade) {
        this.authenticationManager = authenticationManager;
        this.jwtEncoder = jwtEncoder;
        this.validade = validade;
    }

    @PostMapping("/login")
    LoginResponse login(@Valid @RequestBody LoginRequest req) {
        Authentication auth;
        try {
            auth = authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken.unauthenticated(req.usuario(), req.senha()));
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário ou senha incorretos");
        }
        return new LoginResponse(gerarToken(auth.getName()), auth.getName());
    }

    private String gerarToken(String usuario) {
        Instant agora = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("controle-clientes")
                .subject(usuario)
                .issuedAt(agora)
                .expiresAt(agora.plus(validade))
                .build();
        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }
}
