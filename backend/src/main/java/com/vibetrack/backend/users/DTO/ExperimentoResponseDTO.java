package com.vibetrack.backend.users.DTO;

import java.time.LocalDate;

import com.vibetrack.backend.users.DTO.participanteDTO.ParticipanteResponseDTO;
import com.vibetrack.backend.users.DTO.pesquisadorDTO.PesquisadorResponseDTO;
import com.vibetrack.backend.users.Entity.Enums.StatusExperimento;
import java.util.Set;
import java.util.List; // Import necessário

public record ExperimentoResponseDTO(
        Long id,
        String nome,

        String descricaoGeral,
        String resultadoEmocional,

        // VVVV MUDANÇA: AGORA É UMA LISTA VVVV
        List<String> urlsMidia,
        // ^^^^ FIM DA MUDANÇA ^^^^

        LocalDate dataInicio,
        LocalDate dataFim,
        StatusExperimento statusExperimento,
        PesquisadorResponseDTO pesquisador,

        ParticipanteResponseDTO participantePrincipal,

        Set<ParticipanteResponseDTO> participantes
) {}