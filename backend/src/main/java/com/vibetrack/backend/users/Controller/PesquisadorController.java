package com.vibetrack.backend.users.Controller;

// Import do DTO de Alteração de Senha
import com.vibetrack.backend.users.DTO.pesquisadorDTO.ChangePasswordRequestDTO;
import com.vibetrack.backend.users.DTO.pesquisadorDTO.PesquisadorRequestDTO;
import com.vibetrack.backend.users.DTO.pesquisadorDTO.PesquisadorResponseDTO;
import com.vibetrack.backend.users.Entity.Pesquisador;
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
        return ResponseEntity.ok(pesquisadorLogado);
    }

    // Endpoint para BUSCAR um pesquisador por ID
    @GetMapping("/{id}")
    public ResponseEntity<PesquisadorResponseDTO> buscarPesquisadorPorId(@PathVariable Long id) {
        try {
            PesquisadorResponseDTO pesquisador = pesquisadorService.buscarPesquisadorPorId(id);
            return ResponseEntity.ok(pesquisador);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/me/foto")
    public ResponseEntity<String> uploadFotoPerfil(@AuthenticationPrincipal Pesquisador pesquisadorLogado, @RequestParam("file") MultipartFile file) {
        pesquisadorService.atualizarFotoPerfil(pesquisadorLogado, file);
        return ResponseEntity.ok().body("Foto atualizada com sucesso.");
    }

    // vvvv NOVO ENDPOINT: ALTERAR SENHA vvvv
    @PutMapping("/me/senha")
    public ResponseEntity<?> alterarSenha(@AuthenticationPrincipal Pesquisador pesquisadorLogado, @RequestBody @Valid ChangePasswordRequestDTO requestDTO) {
        try {
            // Chama o serviço passando o ID do usuário logado e os dados da requisição
            pesquisadorService.alterarSenha(pesquisadorLogado.getId(), requestDTO);
            return ResponseEntity.ok().body("Senha alterada com sucesso.");
        } catch (IllegalArgumentException e) {
            // Retorna erro 400 se a senha atual estiver errada ou a confirmação falhar
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao alterar senha.");
        }
    }
    // ^^^^ FIM DO NOVO ENDPOINT ^^^^
}