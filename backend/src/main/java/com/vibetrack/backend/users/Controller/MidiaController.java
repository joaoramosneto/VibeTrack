package com.vibetrack.backend.users.Controller;

import com.vibetrack.backend.users.Entity.Midia;
import com.vibetrack.backend.users.Repository.MidiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/midias")
@CrossOrigin(origins = "http://localhost:4200")
public class MidiaController {

    @Autowired
    private MidiaRepository midiaRepository;

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getMidia(@PathVariable Long id) {
        // Busca a mídia no banco pelo ID
        Midia midia = midiaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mídia não encontrada com ID: " + id));

        // Retorna os bytes com o cabeçalho correto (ex: image/png)
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, midia.getTipo())
                .body(midia.getDados());
    }
}