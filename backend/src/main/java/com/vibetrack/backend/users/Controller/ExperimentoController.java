package com.vibetrack.backend.users.Controller; // Ou o caminho mais específico

import com.vibetrack.backend.users.DTO.DashboardDTO.DashboardDTO;
import com.vibetrack.backend.users.Service.ExperimentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;
import com.vibetrack.backend.users.DTO.ExperimentoRequestDTO;
import com.vibetrack.backend.users.DTO.ExperimentoResponseDTO;
import jakarta.validation.Valid;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

@RestController
@RequestMapping("/api/experimentos")
@CrossOrigin(origins = "http://localhost:4200")
public class ExperimentoController {

    @Autowired
    private ExperimentoService experimentoService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ExperimentoResponseDTO> criarExperimento(
            @RequestPart("experimento") @Valid ExperimentoRequestDTO requestDTO,
            @RequestPart(value = "midia", required = false) MultipartFile midiaFile) {

        // Agora, o service precisa de um método que aceite os dados E o arquivo
        ExperimentoResponseDTO responseDTO = experimentoService.salvarComMidia(requestDTO, midiaFile);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PostMapping("/{idExperimento}/participantes/{idParticipante}")
    public ResponseEntity<Void> adicionarParticipanteAoExperimento(
            @PathVariable Long idExperimento,
            @PathVariable Long idParticipante) {

        experimentoService.adicionarParticipante(idExperimento, idParticipante);
        return ResponseEntity.ok().build(); // Retorna 200 OK se a operação for bem-sucedida
    }
    @GetMapping
    public ResponseEntity<List<ExperimentoResponseDTO>> listarTodosExperimentos() {
        // 1. O service.buscarTodos() já retorna a lista de DTOs prontinha.
        List<ExperimentoResponseDTO> experimentosDTO = experimentoService.buscarTodos();

        // 2. Retorne a lista de DTOs com o status 200 OK.
        return ResponseEntity.ok(experimentosDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExperimentoResponseDTO> buscarExperimentoPorId(@PathVariable Long id) {
        // O tratamento de erro pode ser feito aqui com um try-catch ou globalmente com @ControllerAdvice
        try {
            return ResponseEntity.ok(experimentoService.buscarPorId(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint para ATUALIZAR um experimento existente
    @PutMapping("/{id}")
    public ResponseEntity<ExperimentoResponseDTO> atualizarExperimento(@PathVariable Long id, @Valid @RequestBody ExperimentoRequestDTO requestDTO) {
        try {
            ExperimentoResponseDTO responseDTO = experimentoService.atualizar(id, requestDTO);
            return ResponseEntity.ok(responseDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint para DELETAR um experimento
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarExperimento(@PathVariable Long id) {
        try {
            experimentoService.deletar(id);
            return ResponseEntity.noContent().build(); // Retorna 204 No Content (sucesso, sem corpo)
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // No seu ExperimentoController.java

    @GetMapping("/{id}/dashboard")
    public ResponseEntity<DashboardDTO> getDashboardData(@PathVariable Long id) {
        DashboardDTO dashboardData = experimentoService.getDashboardDataMock(id);
        return ResponseEntity.ok(dashboardData);
    }
}