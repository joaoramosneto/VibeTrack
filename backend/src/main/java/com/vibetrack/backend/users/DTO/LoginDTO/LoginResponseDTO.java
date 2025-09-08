package com.vibetrack.backend.users.DTO.LoginDTO;

public record LoginResponseDTO(
        String token,
        String nomeUsuario
) {}
