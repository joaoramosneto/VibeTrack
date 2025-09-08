package com.vibetrack.backend.users.Service;

import com.vibetrack.backend.users.DTO.SensorDTO.DadoSensorRequestDTO;
import com.vibetrack.backend.users.Entity.DadoSensor;
import com.vibetrack.backend.users.Entity.Experimento;
import com.vibetrack.backend.users.Entity.Participante;
import com.vibetrack.backend.users.Repository.DadoSensorRepository;
import com.vibetrack.backend.users.Repository.ExperimentoRepository;
import com.vibetrack.backend.users.Repository.ParticipanteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DadoSensorService {

    @Autowired
    private DadoSensorRepository dadoSensorRepository;

    @Autowired
    private ExperimentoRepository experimentoRepository;

    @Autowired
    private ParticipanteRepository participanteRepository;

    @Transactional
    public DadoSensor salvarDadoSensor(DadoSensorRequestDTO requestDTO) {
        // 1. Busca as entidades relacionadas (Experimento e Participante)
        Experimento experimento = experimentoRepository.findById(requestDTO.experimentoId())
                .orElseThrow(() -> new EntityNotFoundException("Experimento com ID " + requestDTO.experimentoId() + " não encontrado."));

        Participante participante = participanteRepository.findById(requestDTO.participanteId())
                .orElseThrow(() -> new EntityNotFoundException("Participante com ID " + requestDTO.participanteId() + " não encontrado."));

        // 2. Cria e preenche a nova entidade DadoSensor
        DadoSensor novoDado = new DadoSensor();
        novoDado.setTimestamp(requestDTO.timestamp());
        novoDado.setTipo(requestDTO.tipo());
        novoDado.setValor(requestDTO.valor());
        novoDado.setUnidade(requestDTO.unidade());
        novoDado.setExperimento(experimento);
        novoDado.setParticipante(participante);

        // 3. Salva a nova entidade no banco
        return dadoSensorRepository.save(novoDado);
    }

    // Futuramente, outros métodos como buscar dados por experimento, etc.
}
