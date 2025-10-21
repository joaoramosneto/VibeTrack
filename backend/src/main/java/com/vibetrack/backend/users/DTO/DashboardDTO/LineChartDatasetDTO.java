package com.vibetrack.backend.users.DTO.DashboardDTO;

import java.util.List;

// DTO para um dataset de um gráfico de linha (podemos ter várias linhas)
public record LineChartDatasetDTO(
        String label,
        List<Integer> data
) {}
