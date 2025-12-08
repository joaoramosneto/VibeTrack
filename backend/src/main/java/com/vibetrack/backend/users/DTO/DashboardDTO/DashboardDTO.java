package com.vibetrack.backend.users.DTO.DashboardDTO;
import java.util.List;

// DTO principal que agrupa todos os dados do dashboard
public record DashboardDTO(
        LineChartDataDTO frequenciaCardiaca,
        ChartDataDTO distribuicaoEmocoes,
        List<String> urlsMidia
) {}
