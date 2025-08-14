package com.vibetrack.backend.users.Repository;

import com.vibetrack.backend.users.Entity.Participante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipanteRepository extends JpaRepository<Participante, Long> { // Assumindo que o ID do Experimento é Long
    // Aqui você pode adicionar métodos de consulta personalizados se precisar no futuro
    // Ex: List<Experimento> findByNome(String nome);
}