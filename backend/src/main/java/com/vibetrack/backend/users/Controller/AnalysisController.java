package com.vibetrack.backend.users.Controller;

import com.vibetrack.backend.users.DTO.BpmRequestDTO;
import com.vibetrack.backend.users.DTO.AnalysisResponseDTO;
import com.vibetrack.backend.users.Service.AnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analysis") // Um novo caminho base para esta funcionalidade
@CrossOrigin(origins = "http://localhost:4200")
public class AnalysisController {

    // 1. Injeta o "cérebro" que criamos
    @Autowired
    private AnalysisService analysisService;

    /**
     * Recebe um BPM médio e retorna uma análise de emoção.
     */
    @PostMapping("/predict")
    public ResponseEntity<AnalysisResponseDTO> predictEmotion(@RequestBody BpmRequestDTO requestDTO) {

        // 2. Chama o serviço para obter a emoção
        String emocao = analysisService.analisarEmocaoPorBpm(requestDTO.getBpm());

        // 3. Cria o DTO de resposta
        AnalysisResponseDTO response = new AnalysisResponseDTO(emocao);

        // 4. Retorna a resposta para o frontend
        return ResponseEntity.ok(response);
    }
}