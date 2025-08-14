package com.vibetrack.backend.users.Repository; // Ou o caminho mais específico que você escolher

import com.vibetrack.backend.users.Entity.Experimento; // Importe sua entidade Experimento
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExperimentoRepository extends JpaRepository<Experimento, Long> { // Assumindo que o ID do Experimento é Long
    // Aqui você pode adicionar métodos de consulta personalizados se precisar no futuro
    // Ex: List<Experimento> findByNome(String nome);
}