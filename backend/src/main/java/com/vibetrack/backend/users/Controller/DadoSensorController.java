// ARQUIVO ATUALIZADO: DadoSensorController.java
package com.vibetrack.backend.users.Controller;

import com.vibetrack.backend.users.DTO.mobile.ExperimentResultDTO; // <-- MUDANÇA: Importa o DTO correto
import com.vibetrack.backend.users.Service.DadoSensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mobile/results")
public class DadoSensorController {

    @Autowired
    private DadoSensorService dadoSensorService;

    // --- CORREÇÃO PRINCIPAL AQUI ---
    @PostMapping
    public ResponseEntity<?> createDadoSensor(@RequestBody ExperimentResultDTO resultData) {

        // O Spring agora vai conseguir ler o JSON.
        // O método 'createDadoSensor' antigo não serve mais, pois ele espera o DTO errado.
        // Vamos apenas registrar que recebemos os dados.

        // Log para confirmar que os dados chegaram
        System.out.println("-----------------------------------------------------");
        System.out.println("DADOS DO CELULAR RECEBIDOS COM SUCESSO!");
        System.out.println("Usuário ID: " + resultData.getUserId());
        System.out.println("Passos: " + resultData.getHealthData().getSteps());
        System.out.println("Batimentos (Média): " + resultData.getHealthData().getHeartRate());
        System.out.println("-----------------------------------------------------");

        // (No futuro, você pode criar um 'ExperimentResultService'
        // para salvar 'resultData' no banco de dados)

        // Retorna 200 OK para o celular
        return ResponseEntity.ok().build();
    }
}