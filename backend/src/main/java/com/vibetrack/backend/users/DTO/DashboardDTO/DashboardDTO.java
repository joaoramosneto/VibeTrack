package com.vibetrack.backend.users.DTO.DashboardDTO;

// DTO principal que agrupa todos os dados do dashboard
public record DashboardDTO(
        LineChartDataDTO frequenciaCardiaca,
        ChartDataDTO distribuicaoEmocoes
) {}
