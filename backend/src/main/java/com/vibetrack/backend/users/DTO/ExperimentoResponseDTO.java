package com.vibetrack.backend.users.DTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

// VVVV CORREÇÃO: O import antigo foi removido pois agora estão na mesma pasta VVVV
import com.vibetrack.backend.users.DTO.participanteDTO.ParticipanteResponseDTO;
import com.vibetrack.backend.users.DTO.pesquisadorDTO.PesquisadorResponseDTO;
import com.vibetrack.backend.users.Entity.Enums.StatusExperimento;

public record ExperimentoResponseDTO(
        Long id,
        String nome,
        String descricaoGeral,
        String resultadoEmocional,

        List<MidiaResponseDTO> midias,

        LocalDate dataInicio,
        LocalDate dataFim,
        StatusExperimento statusExperimento,
        PesquisadorResponseDTO pesquisador,
        ParticipanteResponseDTO participantePrincipal,
        Set<ParticipanteResponseDTO> participantes
) {}