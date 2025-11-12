package com.vibetrack.backend.users.Controller;

import com.vibetrack.backend.users.DTO.pesquisadorDTO.PesquisadorRequestDTO;
import com.vibetrack.backend.users.DTO.pesquisadorDTO.PesquisadorResponseDTO;
import com.vibetrack.backend.users.Entity.Pesquisador;
import com.vibetrack.backend.users.Repository.PesquisadorRepository;
import com.vibetrack.backend.users.Service.FileStorageService;
import com.vibetrack.backend.users.Service.PesquisadorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/api/pesquisadores")
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

    @GetMapping("/me")
    public ResponseEntity<Pesquisador> getMeuPerfil(@AuthenticationPrincipal Pesquisador pesquisadorLogado) {
        // O @AuthenticationPrincipal injeta diretamente o objeto do usuário logado.
        // É a forma mais elegante e segura de fazer isso.
        // O Spring Security cuida de buscar o usuário a partir do token.

        // Você pode retornar o objeto diretamente ou um DTO (Data Transfer Object)
        // se quiser controlar os campos que são expostos.
        return ResponseEntity.ok(pesquisadorLogado);
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

    @PostMapping("/me/foto")
    public ResponseEntity<String> uploadFotoPerfil(@AuthenticationPrincipal Pesquisador pesquisadorLogado, @RequestParam("file") MultipartFile file) {
        // A única responsabilidade do controller é chamar o serviço.
        pesquisadorService.atualizarFotoPerfil(pesquisadorLogado, file);
        return ResponseEntity.ok().body("Foto atualizada com sucesso.");
    }

}
