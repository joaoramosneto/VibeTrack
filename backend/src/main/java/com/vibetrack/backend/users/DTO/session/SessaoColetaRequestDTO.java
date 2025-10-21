package com.vibetrack.backend.users.DTO.session;

// Record é uma forma concisa de criar DTOs imutáveis
public record SessaoColetaRequestDTO(Long experimentoId, Long participanteId) {
}