// ExperimentoService.java (versão completa)
package com.vibetrack.backend.users.Service;

import com.vibetrack.backend.users.DTO.ExperimentoRequestDTO;
import com.vibetrack.backend.users.DTO.ExperimentoResponseDTO;
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
import org.springframework.transaction.annotation.Transactional; // Importe esta anotação

import java.util.List;

@Service
public class ExperimentoService {

    @Autowired
    private ExperimentoRepository experimentoRepository;
    @Autowired
    private PesquisadorRepository pesquisadorRepository;
    @Autowired
    private ParticipanteRepository participanteRepository;
    @Autowired
    private ExperimentoMapper experimentoMapper;

    @Transactional
    public ExperimentoResponseDTO salvar(ExperimentoRequestDTO requestDTO) {
        // 1. Busca a entidade Pesquisador pelo ID recebido no DTO
        Pesquisador pesquisador = pesquisadorRepository.findById(requestDTO.pesquisadorId())
                .orElseThrow(() -> new EntityNotFoundException("Pesquisador com ID " + requestDTO.pesquisadorId() + " não encontrado."));

        // Validação da regra de negócio
        if (requestDTO.dataInicio().isAfter(requestDTO.dataFim())) {
            throw new IllegalArgumentException("A data de início não pode ser depois da data de fim.");
        }

        // 2. Converte o DTO para a Entidade Experimento
        Experimento experimento = experimentoMapper.toEntity(requestDTO);

        // 3. ASSOCIA o pesquisador encontrado ao novo experimento
        experimento.setPesquisadorResponsavel(pesquisador);

        // 4. Salva o experimento já com a associação
        Experimento experimentoSalvo = experimentoRepository.save(experimento);

        // 5. Retorna o DTO de resposta
        return experimentoMapper.toResponseDTO(experimentoSalvo);
    }

    @Transactional(readOnly = true)
    public List<ExperimentoResponseDTO> buscarTodos() {
        // CORREÇÃO: Converter a lista de Entidades para uma lista de DTOs
        List<Experimento> experimentos = experimentoRepository.findAll();
        return experimentoMapper.toResponseDTOList(experimentos);
    }

    @Transactional(readOnly = true)
    public ExperimentoResponseDTO buscarPorId(Long id) { // CORREÇÃO: Retornar DTO para consistência
        Experimento experimento = experimentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Experimento com ID " + id + " não encontrado."));
        // CORREÇÃO: Converter a Entidade encontrada para DTO
        return experimentoMapper.toResponseDTO(experimento);
    }

    @Transactional
    public ExperimentoResponseDTO atualizar(Long id, ExperimentoRequestDTO requestDTO) { // CORREÇÃO: Receber DTO
        // 1. Busca o experimento existente.
        Experimento experimentoExistente = experimentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Experimento com ID " + id + " não encontrado."));

        // 2. Atualiza os campos do objeto existente com os novos dados do DTO.
        experimentoExistente.setNome(requestDTO.nome());
        experimentoExistente.setDescricao(requestDTO.descricao());
        experimentoExistente.setDataInicio(requestDTO.dataInicio());
        experimentoExistente.setDataFim(requestDTO.dataFim());

        // 3. Salva o objeto atualizado.
        Experimento experimentoAtualizado = experimentoRepository.save(experimentoExistente);

        // 4. Retorna o DTO correspondente
        return experimentoMapper.toResponseDTO(experimentoAtualizado);
    }

    @Transactional
    public void deletar(Long id) {
        if (!experimentoRepository.existsById(id)) {
            throw new EntityNotFoundException("Experimento com ID " + id + " não encontrado para deleção.");
        }
        experimentoRepository.deleteById(id);
    }

    @Transactional
    public void adicionarParticipante(Long idExperimento, Long idParticipante) {
        // 1. Busca a entidade Experimento no banco.
        // Usamos o método que já existe. Ele já lança EntityNotFoundException se não encontrar.
        Experimento experimento = experimentoRepository.findById(idExperimento)
               .orElseThrow(() -> new EntityNotFoundException("Experimento com ID " + idExperimento + " não encontrado."));

        // 2. Busca a entidade Participante no banco.
        Participante participante = participanteRepository.findById(idParticipante)
                .orElseThrow(() -> new EntityNotFoundException("Participante com ID " + idParticipante + " não encontrado."));

        // 3. A MÁGICA: Adiciona o participante ao conjunto (Set) de participantes do experimento.
        experimento.getParticipantes().add(participante);

        // 4. Salva o experimento. Como o método é @Transactional, o Hibernate
        // entende que a coleção de participantes foi modificada e automaticamente
        // insere a nova linha na tabela de junção (experimento_participantes).
        experimentoRepository.save(experimento);
    }
}