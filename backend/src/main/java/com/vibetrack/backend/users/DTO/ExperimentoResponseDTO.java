// Crie este novo arquivo: ExperimentoResponseDTO.java
package com.vibetrack.backend.users.DTO;

import java.time.LocalDate;

import com.vibetrack.backend.users.DTO.participanteDTO.ParticipanteResponseDTO;
import com.vibetrack.backend.users.DTO.pesquisadorDTO.PesquisadorResponseDTO;
import com.vibetrack.backend.users.Entity.Enums.StatusExperimento;
import java.util.Set;

public record ExperimentoResponseDTO(
        Long id,
        String nome,
        String descricao,
        LocalDate dataInicio,
        LocalDate dataFim,
        StatusExperimento statusExperimento,
        PesquisadorResponseDTO pesquisador,
        Set<ParticipanteResponseDTO> participantes,
        String urlMidia // <-- ADICIONE APENAS ESTA LINHA
) {}