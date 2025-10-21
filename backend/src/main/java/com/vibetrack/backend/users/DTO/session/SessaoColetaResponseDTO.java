package com.vibetrack.backend.users.DTO.session;

import java.time.LocalDateTime;

public record SessaoColetaResponseDTO(String codigo, LocalDateTime dataExpiracao) {
}