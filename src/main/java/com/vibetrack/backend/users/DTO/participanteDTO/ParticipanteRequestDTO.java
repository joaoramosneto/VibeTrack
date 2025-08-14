package com.vibetrack.backend.users.DTO.participanteDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import java.time.LocalDate;

// Note a ausência de um campo de senha
public record ParticipanteRequestDTO(
        @NotBlank(message = "O nome completo é obrigatório.")
        String nomeCompleto,

        @NotBlank(message = "O email é obrigatório.")
        @Email(message = "O formato do email é inválido.")
        String email,

        @Past(message = "A data de nascimento deve ser no passado.")
        LocalDate dataNascimento
) {}
