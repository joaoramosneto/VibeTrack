package com.vibetrack.backend.users.Controller;

import com.vibetrack.backend.users.DTO.pesquisadorDTO.PesquisadorRequestDTO;
import com.vibetrack.backend.users.DTO.pesquisadorDTO.PesquisadorResponseDTO;
import com.vibetrack.backend.users.Service.PesquisadorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pesquisadores")
@CrossOrigin(origins = "http://localhost:4200") // Permite requisições do frontend Angular
public class PesquisadorController {

    @Autowired
    private PesquisadorService pesquisadorService;

    // Endpoint para CRIAR um novo pesquisador
    @PostMapping
    public ResponseEntity<PesquisadorResponseDTO> criarPesquisador(@Valid @RequestBody PesquisadorRequestDTO requestDTO) {
        PesquisadorResponseDTO novoPesquisador = pesquisadorService.criarPesquisador(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoPesquisador);
    }

    // Endpoint para LISTAR todos os pesquisadores
    @GetMapping
    public ResponseEntity<List<PesquisadorResponseDTO>> listarTodosPesquisadores() {
        List<PesquisadorResponseDTO> pesquisadores = pesquisadorService.listarTodosPesquisadores();
        return ResponseEntity.ok(pesquisadores);
    }

    // Endpoint para BUSCAR um pesquisador por ID
    @GetMapping("/{id}")
    public ResponseEntity<PesquisadorResponseDTO> buscarPesquisadorPorId(@PathVariable Long id) {
        // O tratamento de erro (EntityNotFoundException) pode ser melhorado com @ControllerAdvice,
        // mas por enquanto um try-catch funciona.
        try {
            PesquisadorResponseDTO pesquisador = pesquisadorService.buscarPesquisadorPorId(id);
            return ResponseEntity.ok(pesquisador);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}