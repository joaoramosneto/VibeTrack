package com.vibetrack.backend.users.Repository;

import com.vibetrack.backend.users.Entity.DadoSensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DadoSensorRepository extends JpaRepository<DadoSensor, Long> {

    // No futuro, podemos adicionar aqui métodos de consulta personalizados, como:
    // List<DadoSensor> findByExperimentoIdAndParticipanteIdOrderByTimestampAsc(Long experimentoId, Long participanteId);
    // Este metodo buscaria todos os dados de um participante específico em um experimento, ordenados pelo tempo.
}