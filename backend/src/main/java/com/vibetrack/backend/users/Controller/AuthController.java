package com.vibetrack.backend.users.Controller;

import com.vibetrack.backend.users.DTO.LoginDTO.LoginRequestDTO;
import com.vibetrack.backend.users.Entity.Pesquisador;
import com.vibetrack.backend.users.Service.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import com.vibetrack.backend.users.DTO.LoginDTO.LoginResponseDTO;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    // 1. Mude o tipo de retorno de ResponseEntity<String> para ResponseEntity<LoginResponseDTO>
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO loginDTO) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(loginDTO.email(), loginDTO.senha());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var pesquisador = (Pesquisador) auth.getPrincipal();
        var token = tokenService.gerarToken(pesquisador);

        // 2. Crie uma instância do novo DTO de resposta
        var loginResponse = new LoginResponseDTO(token, pesquisador.getNome());

        // 3. Retorne o DTO no corpo da resposta
        return ResponseEntity.ok(loginResponse);
    }
} 