package com.vibetrack.backend.users.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.vibetrack.backend.users.Entity.Pesquisador;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    // 1. Injeta o valor da nossa chave secreta do application.properties
    @Value("${api.security.token.secret}")
    private String secret;

    public String gerarToken(Pesquisador pesquisador) {
        try {
            // 2. Define o algoritmo de assinatura usando nossa chave secreta
            Algorithm algoritmo = Algorithm.HMAC256(secret);

            // 3. Cria e assina o token
            String token = JWT.create()
                    .withIssuer("VibeTrack API") // Emissor do token
                    .withSubject(pesquisador.getEmail()) // Assunto (o email/login do usuário)
                    .withExpiresAt(gerarDataDeExpiracao()) // Define a data de validade
                    // Você pode adicionar "claims" extras se precisar, como o ID do usuário
                    // .withClaim("id", pesquisador.getId())
                    .sign(algoritmo);
            return token;
        } catch (JWTCreationException exception){
            throw new RuntimeException("Erro ao gerar token JWT", exception);
        }
    }

    // Este método será usado futuramente pelo nosso filtro de segurança
    public String validarToken(String token) {
        try {
            Algorithm algoritmo = Algorithm.HMAC256(secret);
            return JWT.require(algoritmo)
                    .withIssuer("VibeTrack API")
                    .build()
                    .verify(token) // Valida o token (assinatura, data de expiração, etc.)
                    .getSubject(); // Se for válido, retorna o "subject" (nosso email)
        } catch (JWTVerificationException exception){
            return ""; // Retorna vazio se o token for inválido
        }
    }

    private Instant gerarDataDeExpiracao() {
        // Token expira em 2 horas (ajuste conforme necessário)
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}