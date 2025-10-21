package com.vibetrack.backend.users.Service; // Ou o pacote de serviços

import com.vibetrack.backend.users.DTO.mobile.ExperimentResultDTO;
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
import java.util.List;
import java.util.Optional;

@Service
public class DadosBiometricosService {

    @Autowired
    private ParticipanteRepository participanteRepository;

    @Autowired
    private DadoBiometricoRepository dadoBiometricoRepository;

    @Transactional
    public void salvarDados(ExperimentResultDTO resultDTO) {
        // 1. Encontrar o participante pelo ID recebido do app mobile
        Long participanteId = Long.parseLong(resultDTO.getUserId());
        Participante participante = participanteRepository.findById(participanteId)
                .orElseThrow(() -> new EntityNotFoundException("Participante com ID " + participanteId + " não encontrado."));

        // 2. Encontrar o experimento ativo para este participante
        Optional<Experimento> experimentoAtivoOpt = participante.getExperimentos().stream()
                .filter(exp -> exp.getStatusExperimento() == StatusExperimento.EM_ANDAMENTO)
                .findFirst();

        if (experimentoAtivoOpt.isEmpty()) {
            // Se não houver experimento ativo, não podemos salvar os dados.
            // Aqui você pode lançar uma exceção ou apenas logar um aviso.
            System.out.println("Aviso: Nenhum experimento ativo encontrado para o participante " + participanteId + ". Dados não foram salvos.");
            return;
        }
        Experimento experimento = experimentoAtivoOpt.get();

        // 3. Iterar sobre os dados de frequência cardíaca e salvá-los
        if (resultDTO.getHealthData() != null && resultDTO.getHealthData().getHeartRate() != null) {
            resultDTO.getHealthData().getHeartRate().forEach(heartRateDTO -> {
                DadoBiometrico dado = new DadoBiometrico();
                dado.setParticipante(participante);
                dado.setExperimento(experimento);
                dado.setTipoDado("FREQUENCIA_CARDIACA");
                dado.setValor((double) heartRateDTO.getValue());
                // Converte o timestamp (milissegundos) para LocalDateTime
                dado.setTimestamp(LocalDateTime.ofInstant(Instant.ofEpochMilli(heartRateDTO.getTimestamp()), ZoneId.systemDefault()));
                dadoBiometricoRepository.save(dado);
            });
        }
        // Você pode adicionar uma lógica similar para salvar os "passos" (steps) se necessário
    }
}