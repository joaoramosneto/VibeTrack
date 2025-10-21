package com.vibetrack.backend.users.Controller;

import com.vibetrack.backend.users.DTO.mobile.ExperimentResultDTO;
import com.vibetrack.backend.users.Service.DadosBiometricosService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/results")
public class DadosBiometricosController {

    @Autowired
    private DadosBiometricosService dadosBiometricosService;

    @PostMapping
    public ResponseEntity<String> receberDadosDoMobile(@RequestBody ExperimentResultDTO experimentResult) {
        try {
            dadosBiometricosService.salvarDados(experimentResult);
            return ResponseEntity.ok("Dados recebidos e processados com sucesso!");
        } catch (EntityNotFoundException e) {
            // Retorna um erro amigável se o participante não for encontrado
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            // Retorna um erro genérico para outros problemas
            return ResponseEntity.status(500).body("Erro interno ao processar os dados.");
        }
    }
}