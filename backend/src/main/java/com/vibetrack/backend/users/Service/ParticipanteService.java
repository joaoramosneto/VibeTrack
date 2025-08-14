package com.vibetrack.backend.users.Service;

import com.vibetrack.backend.users.DTO.participanteDTO.ParticipanteRequestDTO;
import com.vibetrack.backend.users.DTO.participanteDTO.ParticipanteResponseDTO;
import com.vibetrack.backend.users.Entity.Participante;
import com.vibetrack.backend.users.Mapper.ParticipanteMapper;
import com.vibetrack.backend.users.Repository.ParticipanteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ParticipanteService {

    @Autowired
    private ParticipanteRepository participanteRepository;

    @Autowired
    private ParticipanteMapper participanteMapper;

    @Transactional
    public ParticipanteResponseDTO criarParticipante(ParticipanteRequestDTO requestDTO) {
        // Converte o DTO para a entidade
        Participante participante = participanteMapper.toEntity(requestDTO);

        // Salva a entidade no banco
        Participante novoParticipante = participanteRepository.save(participante);

        // Retorna o DTO de resposta
        return participanteMapper.toResponseDTO(novoParticipante);
    }

    @Transactional(readOnly = true)
    public List<ParticipanteResponseDTO> listarTodosParticipantes() {
        List<Participante> participantes = participanteRepository.findAll();
        return participanteMapper.toResponseDTOList(participantes);
    }

    @Transactional(readOnly = true)
    public ParticipanteResponseDTO buscarParticipantePorId(Long id) {
        Participante participante = participanteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Participante com ID " + id + " não encontrado."));
        return participanteMapper.toResponseDTO(participante);
    }

    @Transactional
    public ParticipanteResponseDTO atualizarParticipante(Long id, ParticipanteRequestDTO requestDTO) {
        // Busca o participante existente
        Participante participanteExistente = participanteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Participante com ID " + id + " não encontrado."));

        // Atualiza os campos
        participanteExistente.setNomeCompleto(requestDTO.nomeCompleto());
        participanteExistente.setEmail(requestDTO.email());
        participanteExistente.setDataNascimento(requestDTO.dataNascimento());

        // Salva e retorna o DTO
        Participante participanteAtualizado = participanteRepository.save(participanteExistente);
        return participanteMapper.toResponseDTO(participanteAtualizado);
    }

    @Transactional
    public void deletarParticipante(Long id) {
        if (!participanteRepository.existsById(id)) {
            throw new EntityNotFoundException("Participante com ID " + id + " não encontrado para deleção.");
        }
        participanteRepository.deleteById(id);
    }
}