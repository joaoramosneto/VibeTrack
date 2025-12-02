package com.vibetrack.backend.users.DTO; // VVVV CORREÇÃO: REMOVIDO .midia VVVV

public record MidiaResponseDTO(
        Long id,
        String nome,
        String tipo,
        String url
) {}