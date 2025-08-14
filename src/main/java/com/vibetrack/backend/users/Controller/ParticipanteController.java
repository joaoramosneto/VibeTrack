package com.vibetrack.backend.users.Controller;

import com.vibetrack.backend.users.DTO.participanteDTO.ParticipanteRequestDTO;
import com.vibetrack.backend.users.DTO.participanteDTO.ParticipanteResponseDTO;
import com.vibetrack.backend.users.Service.ParticipanteService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/participantes")
@CrossOrigin(origins = "http://localhost:4200")
public class ParticipanteController {

    @Autowired
    private ParticipanteService participanteService;

    // Endpoint para CRIAR um novo participante
    @PostMapping
    public ResponseEntity<ParticipanteResponseDTO> criarParticipante(@Valid @RequestBody ParticipanteRequestDTO requestDTO) {
        ParticipanteResponseDTO novoParticipante = participanteService.criarParticipante(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoParticipante);
    }

    // Endpoint para LISTAR todos os participantes
    @GetMapping
    public ResponseEntity<List<ParticipanteResponseDTO>> listarTodosParticipantes() {
        List<ParticipanteResponseDTO> participantes = participanteService.listarTodosParticipantes();
        return ResponseEntity.ok(participantes);
    }

    // Endpoint para BUSCAR um participante por ID
    @GetMapping("/{id}")
    public ResponseEntity<ParticipanteResponseDTO> buscarParticipantePorId(@PathVariable Long id) {
        try {
            ParticipanteResponseDTO participante = participanteService.buscarParticipantePorId(id);
            return ResponseEntity.ok(participante);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint para ATUALIZAR um participante existente
    @PutMapping("/{id}")
    public ResponseEntity<ParticipanteResponseDTO> atualizarParticipante(@PathVariable Long id, @Valid @RequestBody ParticipanteRequestDTO requestDTO) {
        try {
            ParticipanteResponseDTO participanteAtualizado = participanteService.atualizarParticipante(id, requestDTO);
            return ResponseEntity.ok(participanteAtualizado);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint para DELETAR um participante
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarParticipante(@PathVariable Long id) {
        try {
            participanteService.deletarParticipante(id);
            return ResponseEntity.noContent().build(); // Retorna 204 No Content
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}