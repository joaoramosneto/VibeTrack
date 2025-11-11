package com.vibetrack.backend.users.Controller;

import com.vibetrack.backend.users.DTO.LoginDTO.LoginRequestDTO;
import com.vibetrack.backend.users.DTO.LoginDTO.LoginResponseDTO;
import com.vibetrack.backend.users.Entity.Pesquisador;
import com.vibetrack.backend.users.Service.PesquisadorService;
import com.vibetrack.backend.users.Service.TokenService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus; // Importar HttpStatus
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

// Imports dos nossos DTOs
import com.vibetrack.backend.users.DTO.ForgotPasswordRequestDTO;
import com.vibetrack.backend.users.DTO.ResetPasswordRequestDTO; // O DTO que já criámos

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PesquisadorService pesquisadorService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO loginDTO) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(loginDTO.email(), loginDTO.senha());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var pesquisador = (Pesquisador) auth.getPrincipal();
        var token = tokenService.gerarToken(pesquisador);
        var loginResponse = new LoginResponseDTO(token, pesquisador.getNome(), pesquisador.getId());
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/verificar-codigo")
    public ResponseEntity<String> verificarCodigo(@RequestParam String codigo) {
        try {
            pesquisadorService.verificarCodigoEAtivarConta(codigo);
            return ResponseEntity.ok("Conta verificada com sucesso! Você já pode fazer o login.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequestDTO requestDTO) {
        try {
            pesquisadorService.iniciarResetSenha(requestDTO.getEmail());
        } catch (Exception e) {
            System.err.println("Falha ao processar reset de senha: " + e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    // vvvv ESTE É O MÉTODO ATUALIZADO (A LÓGICA FINAL) vvvv
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequestDTO requestDTO) {
        try {
            // 1. CHAMA O "CÉREBRO" (O SERVIÇO)
            pesquisadorService.finalizarResetSenha(requestDTO.getToken(), requestDTO.getNovaSenha());

            // 2. Se tudo deu certo, retorna 200 OK
            return ResponseEntity.ok().build();

        } catch (EntityNotFoundException e) {
            // 3. Se o token for inválido
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // Ex: "Token inválido."

        } catch (IllegalArgumentException e) {
            // 4. Se o token estiver expirado
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()); // Ex: "Token expirado."

        } catch (Exception e) {
            // 5. Qualquer outro erro
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno ao redefinir a senha.");
        }
    }
}