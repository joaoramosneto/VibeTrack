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

    // Endpoint para DELETAR um pesquisador por ID (NOVO)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPesquisador(@PathVariable Long id) {
        try {
            pesquisadorService.deletarPesquisador(id);
            return ResponseEntity.noContent().build(); // Retorna 204 No Content para sucesso na deleção
        } catch (Exception e) {
            // Em um ambiente de produção, você pode retornar 404 (Not Found) se o ID não existir
            // ou 403/409 se houver regras de negócio que impedem a deleção.
            return ResponseEntity.internalServerError().build();
        }
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

    // Endpoint: ALTERAR SENHA
    @PutMapping("/me/senha")
    public ResponseEntity<?> alterarSenha(@AuthenticationPrincipal Pesquisador pesquisadorLogado, @RequestBody @Valid ChangePasswordRequestDTO requestDTO) {
        try {
            pesquisadorService.alterarSenha(pesquisadorLogado.getId(), requestDTO);
            return ResponseEntity.ok().body("Senha alterada com sucesso.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao alterar senha.");
        }
    }
}