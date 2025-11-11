package com.vibetrack.backend.users.Service;

import com.vibetrack.backend.users.DTO.mobile.ExperimentResultDTO;
// Importe os DTOs que vamos usar
import com.vibetrack.backend.users.DTO.mobile.HealthDataDTO;
import com.vibetrack.backend.users.DTO.mobile.HeartRateDTO;
import com.vibetrack.backend.users.Entity.DadoBiometrico;
import com.vibetrack.backend.users.Entity.Enums.StatusExperimento;
import com.vibetrack.backend.users.Entity.Experimento;
import com.vibetrack.backend.users.Entity.Participante;
import com.vibetrack.backend.users.Repository.DadoBiometricoRepository;
import com.vibetrack.backend.users.Repository.ParticipanteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Service
public class DadosBiometricosService {

    @Autowired
    private ParticipanteRepository participanteRepository;

    @Autowired
    private DadoBiometricoRepository dadoBiometricoRepository;

    @Transactional
    public void salvarDados(ExperimentResultDTO resultDTO) {
        // 1. Encontrar o participante
        // NOTA: O app mobile envia o "pairing code" (String) como UserId.
        // O backend parece esperar um Long. Isso precisa ser consistente.
        // Por enquanto, vamos assumir que o 'userId' é um ID numérico em String.
        Long participanteId;
        try {
            participanteId = Long.parseLong(resultDTO.getUserId());
        } catch (NumberFormatException e) {
            System.out.println("Erro: UserId recebido não é um número: " + resultDTO.getUserId());
            return;
        }

        Participante participante = participanteRepository.findById(participanteId)
                .orElseThrow(() -> new EntityNotFoundException("Participante com ID " + participanteId + " não encontrado."));

        // 2. Encontrar o experimento ativo para este participante (lógica original)
        Optional<Experimento> experimentoAtivoOpt = participante.getExperimentos().stream()
                .filter(exp -> exp.getStatusExperimento() == StatusExperimento.EM_ANDAMENTO)
                .findFirst();

        if (experimentoAtivoOpt.isEmpty()) {
            System.out.println("Aviso: Nenhum experimento ativo encontrado para o participante " + participanteId + ". Dados não foram salvos.");
            return;
        }
        Experimento experimento = experimentoAtivoOpt.get();

        // 3. Pegar o timestamp da coleta
        // O app mobile envia uma String ISO 8601 (ex: "2025-11-11T14:30:00Z")
        LocalDateTime timestamp;
        try {
            Instant instant = Instant.parse(resultDTO.getDate());
            timestamp = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        } catch (Exception e) {
            System.out.println("Aviso: Não foi possível parsear a data do DTO. Usando data/hora atual. Erro: " + e.getMessage());
            timestamp = LocalDateTime.now();
        }

        // 4. Pegar os dados de saúde (Singular, graças à Correção 1)
        HealthDataDTO healthData = resultDTO.getHealthData();
        if (healthData == null) {
            System.out.println("Aviso: HealthData nulo. Nada para salvar.");
            return;
        }

        // 5. Salvar os PASSOS
        DadoBiometrico dadoSteps = new DadoBiometrico();
        dadoSteps.setParticipante(participante);
        dadoSteps.setExperimento(experimento);
        dadoSteps.setTipoDado("PASSOS");
        dadoSteps.setValor((double) healthData.getSteps());
        dadoSteps.setTimestamp(timestamp);
        dadoBiometricoRepository.save(dadoSteps);

        // 6. Salvar os dados de FREQUÊNCIA CARDÍACA (Singular)
        HeartRateDTO heartRate = healthData.getHeartRate();
        if (heartRate != null) {
            // Salvar FC Mínima (Resting)
            DadoBiometrico dadoHrMin = new DadoBiometrico();
            dadoHrMin.setParticipante(participante);
            dadoHrMin.setExperimento(experimento);
            dadoHrMin.setTipoDado("FC_MINIMA");
            dadoHrMin.setValor((double) heartRate.getResting());
            dadoHrMin.setTimestamp(timestamp);
            dadoBiometricoRepository.save(dadoHrMin);

            // Salvar FC Média
            DadoBiometrico dadoHrAvg = new DadoBiometrico();
            dadoHrAvg.setParticipante(participante);
            dadoHrAvg.setExperimento(experimento);
            dadoHrAvg.setTipoDado("FC_MEDIA");
            dadoHrAvg.setValor((double) heartRate.getAverage());
            dadoHrAvg.setTimestamp(timestamp);
            dadoBiometricoRepository.save(dadoHrAvg);

            // Salvar FC Máxima
            DadoBiometrico dadoHrMax = new DadoBiometrico();
            dadoHrMax.setParticipante(participante);
            dadoHrMax.setExperimento(experimento);
            dadoHrMax.setTipoDado("FC_MAXIMA");
            dadoHrMax.setValor((double) heartRate.getMax());
            dadoHrMax.setTimestamp(timestamp);
            dadoBiometricoRepository.save(dadoHrMax);
        }

        System.out.println("Sucesso: Dados biométricos salvos para o participante " + participanteId);
    }
}