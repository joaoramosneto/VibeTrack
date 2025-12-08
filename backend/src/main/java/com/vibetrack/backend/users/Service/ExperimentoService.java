package com.vibetrack.backend.users.Service;

import com.vibetrack.backend.users.DTO.DashboardDTO.DashboardDTO;
import com.vibetrack.backend.users.DTO.DashboardDTO.LineChartDataDTO;
import com.vibetrack.backend.users.DTO.DashboardDTO.LineChartDatasetDTO;
import com.vibetrack.backend.users.DTO.DashboardDTO.ChartDataDTO;
import com.vibetrack.backend.users.DTO.ExperimentoRequestDTO;
import com.vibetrack.backend.users.DTO.ExperimentoResponseDTO;
import com.vibetrack.backend.users.Entity.Enums.StatusExperimento;
import com.vibetrack.backend.users.Entity.Experimento;
import com.vibetrack.backend.users.Entity.Participante;
import com.vibetrack.backend.users.Entity.Pesquisador;
import com.vibetrack.backend.users.Mapper.ExperimentoMapper;
import com.vibetrack.backend.users.Repository.ExperimentoRepository;
import com.vibetrack.backend.users.Repository.ParticipanteRepository;
import com.vibetrack.backend.users.Repository.PesquisadorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.vibetrack.backend.users.Entity.DadoBiometrico;
import com.vibetrack.backend.users.Repository.DadoBiometricoRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value; 


@Service
public class ExperimentoService {

    @Value("${app.media.base-url}")
    private String mediaBaseUrl;
    

    @Transactional
    public ExperimentoResponseDTO salvarComMidia(ExperimentoRequestDTO requestDTO, List<MultipartFile> midiaFiles) {
        // ... (código existente)
        if (midiaFiles != null && !midiaFiles.isEmpty()) {
            List<String> urls = new ArrayList<>();
            for (MultipartFile file : midiaFiles) {
                if (!file.isEmpty()) {
                    String nomeArquivo = fileStorageService.storeFile(file);
                  
                    urls.add(mediaBaseUrl + "/" + nomeArquivo); 
                }
            }
            experimento.setUrlsMidia(urls);
        }
        // ...
    }


    @Transactional
    public ExperimentoResponseDTO atualizarMidia(Long id, List<MultipartFile> midiaFiles) {
        Experimento experimento = experimentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Experimento com ID " + id + " não encontrado."));
        // ... (código existente)
        if (midiaFiles != null && !midiaFiles.isEmpty()) {
            // ...
            for (MultipartFile file : midiaFiles) {
                if (!file.isEmpty()) {
                    String nomeArquivo = fileStorageService.storeFile(file);
                  
                    urls.add(mediaBaseUrl + "/" + nomeArquivo);
                }
            }
            experimento.setUrlsMidia(urls);
        } else {
            experimento.setUrlsMidia(new ArrayList<>());
        }
        // ...
    }
    

    @Transactional(readOnly = true)
    public DashboardDTO getDashboardData(Long experimentoId) {
        // 🔍 Busca o experimento completo para obter os dados da mídia
        Experimento experimento = experimentoRepository.findById(experimentoId)
                .orElseThrow(() -> new EntityNotFoundException("Experimento com ID " + experimentoId + " não encontrado."));

        // ... (lógica de frequencia e emoções)
        
        var datasetFrequencia = new LineChartDatasetDTO("Batimentos por Minuto (BPM)", dadosBatimentos);
        var graficoFrequencia = new LineChartDataDTO(labelsTipos, List.of(datasetFrequencia));
        var graficoEmocoes = new ChartDataDTO(List.of(), List.of());

    
        return new DashboardDTO(
            graficoFrequencia, 
            graficoEmocoes,
            experimento.getUrlsMidia() 
        );
    }
}