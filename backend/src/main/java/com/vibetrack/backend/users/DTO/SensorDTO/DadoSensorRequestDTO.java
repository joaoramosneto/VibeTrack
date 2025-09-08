package com.vibetrack.backend.users.DTO.SensorDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public record DadoSensorRequestDTO(
        @NotNull(message = "O timestamp é obrigatório.")
        Instant timestamp,

        @NotBlank(message = "O tipo de dado é obrigatório.")
        String tipo,

        @NotNull(message = "O valor da medição é obrigatório.")
        Double valor,

        String unidade,

        @NotNull(message = "O ID do experimento é obrigatório.")
        Long experimentoId,

        @NotNull(message = "O ID do participante é obrigatório.")
        Long participanteId
) {}