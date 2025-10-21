package com.vibetrack.backend.users.DTO.DashboardDTO;

import java.util.List;

// DTO para os dados de um gráfico de linha completo
public record LineChartDataDTO(
        List<String> labels, // Eixo X (tempo)
        List<LineChartDatasetDTO> datasets
) {}
