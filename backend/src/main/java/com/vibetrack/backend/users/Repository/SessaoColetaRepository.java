package com.vibetrack.backend.users.Repository; // Ou o seu pacote de repositórios

import com.vibetrack.backend.users.Entity.SessaoColeta;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SessaoColetaRepository extends JpaRepository<SessaoColeta, Long> {
    // Método para encontrar uma sessão ativa pelo código
    Optional<SessaoColeta> findByCodigoAndAtivaTrue(String codigo);
}