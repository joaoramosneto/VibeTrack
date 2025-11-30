package com.vibetrack.backend.users.DTO;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List; // Import necessário

public record ExperimentoRequestDTO(
        @NotBlank(message = "O nome não pode ser vazio.")
        @Size(min = 3, message = "O nome deve ter no mínimo 3 caracteres.")
        String nome,

        String statusExperimento,

        String descricaoAmbiente,

        String tipoEmocao,

        // VVVV MUDANÇA: AGORA É UMA LISTA DE STRINGS VVVV
        List<String> urlsMidia,
        // ^^^^ FIM DA MUDANÇA ^^^^

        @NotNull(message = "A data de início é obrigatória.")
        @FutureOrPresent(message = "A data de início não pode ser no passado.")
        LocalDate dataInicio,

        @NotNull(message = "A data de fim é obrigatória.")
        LocalDate dataFim,

        @NotNull(message = "O ID do pesquisador é obrigatório.")
        Long pesquisadorId,

        // Mantemos o participanteId
        Long participanteId
) {}