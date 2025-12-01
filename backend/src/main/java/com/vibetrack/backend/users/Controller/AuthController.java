package com.vibetrack.backend.users.Controller;

import com.vibetrack.backend.users.DTO.LoginDTO.LoginRequestDTO;
import com.vibetrack.backend.users.DTO.LoginDTO.LoginResponseDTO;
import com.vibetrack.backend.users.Entity.Pesquisador;
import com.vibetrack.backend.users.Service.PesquisadorService; // <-- NOVO IMPORT
import com.vibetrack.backend.users.Service.TokenService;
import jakarta.persistence.EntityNotFoundException; // <-- NOVO IMPORT
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    // vvvv INJEÇÃO DO NOVO SERVIÇO vvvv
    @Autowired
    private PesquisadorService pesquisadorService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO loginDTO) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(loginDTO.email(), loginDTO.senha());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var pesquisador = (Pesquisador) auth.getPrincipal();
        var token = tokenService.gerarToken(pesquisador);

        // A resposta do login agora inclui o ID do pesquisador
        var loginResponse = new LoginResponseDTO(token, pesquisador.getNome(), pesquisador.getId());

        return ResponseEntity.ok(loginResponse);
    }

    // vvvv MÉTODO DE VERIFICAÇÃO QUE ADICIONAMOS vvvv
    @PostMapping("/verificar-codigo")
    public ResponseEntity<String> verificarCodigo(@RequestParam String codigo) {
        try {
            pesquisadorService.verificarCodigoEAtivarConta(codigo);
            return ResponseEntity.ok("Conta verificada com sucesso! Você já pode fazer o login.");
        } catch (IllegalArgumentException e) {
            // Retorna um erro 400 (Bad Request) se o código for inválido ou expirado
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (EntityNotFoundException e) {
            // Retorna um erro 404 (Not Found) se o código não for encontrado
            return ResponseEntity.notFound().build();
        }
    }
}
