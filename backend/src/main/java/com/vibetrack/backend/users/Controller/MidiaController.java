package com.vibetrack.backend.users.Controller;

import com.vibetrack.backend.users.Entity.Midia;
import com.vibetrack.backend.users.Repository.MidiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/midias")
@CrossOrigin(origins = "http://localhost:4200")
public class MidiaController {

    @Autowired
    private MidiaRepository midiaRepository;

    // Endpoint de Download (Já existia)
    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getMidia(@PathVariable Long id) {
        Midia midia = midiaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mídia não encontrada: " + id));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, midia.getTipo())
                .body(midia.getDados());
    }

    // VVVV NOVO ENDPOINT: DELETAR MÍDIA VVVV
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarMidia(@PathVariable Long id) {
        if (midiaRepository.existsById(id)) {
            midiaRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    // ^^^^ FIM ^^^^
}