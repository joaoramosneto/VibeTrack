package com.vibetrack.backend.users.Controller;

import com.vibetrack.backend.users.DTO.DashboardDTO.DashboardDTO;
import com.vibetrack.backend.users.DTO.ExperimentoRequestDTO;
import com.vibetrack.backend.users.DTO.ExperimentoResponseDTO;
import com.vibetrack.backend.users.Service.ExperimentoService;
import jakarta.validation.Valid;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
            // VVVV MUDANÇA: Agora aceita uma LISTA de arquivos VVVV
            @RequestPart(value = "midia", required = false) List<MultipartFile> midiaFiles) {

        ExperimentoResponseDTO responseDTO = experimentoService.salvarComMidia(requestDTO, midiaFiles);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PutMapping(value = "/{id}/midia", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ExperimentoResponseDTO> alterarMidiaExperimento(
            @PathVariable Long id,
            // VVVV MUDANÇA: Agora aceita uma LISTA de arquivos VVVV
            @RequestPart("midia") List<MultipartFile> midiaFiles) {

        try {
            ExperimentoResponseDTO responseDTO = experimentoService.atualizarMidia(id, midiaFiles);
            return ResponseEntity.ok(responseDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    // ^^^^ FIM DAS MUDANÇAS ^^^^

    @PostMapping("/{idExperimento}/participantes/{idParticipante}")
    public ResponseEntity<Void> adicionarParticipanteAoExperimento(
            @PathVariable Long idExperimento,
            @PathVariable Long idParticipante) {

        experimentoService.adicionarParticipante(idExperimento, idParticipante);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<ExperimentoResponseDTO>> listarTodosExperimentos() {
        List<ExperimentoResponseDTO> experimentosDTO = experimentoService.buscarTodos();
        return ResponseEntity.ok(experimentosDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExperimentoResponseDTO> buscarExperimentoPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(experimentoService.buscarPorId(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExperimentoResponseDTO> atualizarExperimento(@PathVariable Long id, @Valid @RequestBody ExperimentoRequestDTO requestDTO) {
        try {
            ExperimentoResponseDTO responseDTO = experimentoService.atualizar(id, requestDTO);
            return ResponseEntity.ok(responseDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarExperimento(@PathVariable Long id) {
        try {
            experimentoService.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/dashboard")
    public ResponseEntity<DashboardDTO> getDashboardData(@PathVariable Long id) {
        try {
            DashboardDTO dashboardData = experimentoService.getDashboardData(id);
            return ResponseEntity.ok(dashboardData);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}