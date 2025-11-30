package com.vibetrack.backend.users.DTO.pesquisadorDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequestDTO(
        @NotBlank(message = "A senha atual é obrigatória.")
        String senhaAtual,

        @NotBlank(message = "A nova senha é obrigatória.")
        @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres.")
        String novaSenha,

        @NotBlank(message = "A confirmação da senha é obrigatória.")
        String confirmacaoSenha
) {}