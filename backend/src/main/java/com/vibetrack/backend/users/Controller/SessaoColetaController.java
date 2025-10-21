package com.vibetrack.backend.users.Controller; // Ou o seu pacote de controllers

import com.vibetrack.backend.users.DTO.session.*;
import com.vibetrack.backend.users.Service.SessaoColetaService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/sessoes")
public class SessaoColetaController {

    @Autowired
    private SessaoColetaService sessaoColetaService;

    // Endpoint para o frontend WEB chamar
    @PostMapping("/iniciar")
    public ResponseEntity<SessaoColetaResponseDTO> iniciarSessao(@RequestBody SessaoColetaRequestDTO request) {
        SessaoColetaResponseDTO response = sessaoColetaService.criarSessao(request);
        return ResponseEntity.ok(response);
    }

    // Endpoint para o frontend MOBILE chamar
    @PostMapping("/validar")
    public ResponseEntity<ValidacaoSessaoResponseDTO> validarSessao(@RequestBody ValidacaoSessaoRequestDTO request) {
        try {
            ValidacaoSessaoResponseDTO response = sessaoColetaService.validarSessao(request);
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(null); // Retorna 400 Bad Request se o código for inválido/expirado
        }
    }
}