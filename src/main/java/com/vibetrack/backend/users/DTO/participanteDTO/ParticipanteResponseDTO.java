package com.vibetrack.backend.users.DTO.participanteDTO;

import java.time.LocalDate;

public record ParticipanteResponseDTO(
        Long id,
        String nomeCompleto,
        String email,
        LocalDate dataNascimento
) {}