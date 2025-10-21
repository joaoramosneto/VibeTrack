package com.vibetrack.backend.users.DTO.DashboardDTO;

// No pacote dto/dashboard

import java.util.List;

// DTO para os dados de um gráfico de pizza/rosca
public record ChartDataDTO(
        List<String> labels,
        List<Integer> data
) {}

