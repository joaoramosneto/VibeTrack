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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile; // NOVO IMPORT

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

    // NOVO MÉTODO PARA LIDAR COM UPLOAD DE MÍDIA
    @Transactional
    public ExperimentoResponseDTO salvarComMidia(ExperimentoRequestDTO requestDTO, MultipartFile midiaFile) {
        // 1. Reutilizamos a mesma lógica de validação e busca do seu método salvar original
        Pesquisador pesquisador = pesquisadorRepository.findById(requestDTO.pesquisadorId())
                .orElseThrow(() -> new EntityNotFoundException("Pesquisador com ID " + requestDTO.pesquisadorId() + " não encontrado."));

        if (requestDTO.dataInicio().isAfter(requestDTO.dataFim())) {
            throw new IllegalArgumentException("A data de início não pode ser depois da data de fim.");
        }

        Experimento experimento = experimentoMapper.toEntity(requestDTO);
        experimento.setPesquisadorResponsavel(pesquisador);

        // 2. Lógica para salvar a mídia, se ela foi enviada
        if (midiaFile != null && !midiaFile.isEmpty()) {
            // TODO: Implementar a lógica para salvar o arquivo
            // Isso geralmente envolve chamar um outro serviço (ex: FileStorageService)
            // que salva o arquivo em uma pasta no servidor ou em um serviço de nuvem (como AWS S3)
            // e retorna o caminho ou a URL do arquivo.
            // Por enquanto, vamos apenas imprimir no console para confirmar que o arquivo chegou.
            System.out.println("Arquivo de mídia recebido: " + midiaFile.getOriginalFilename());
            System.out.println("Tamanho do arquivo: " + midiaFile.getSize() + " bytes");

            // Exemplo de como você associaria a mídia ao experimento (requer entidade Midia):
            // Midia midiaSalva = fileStorageService.salvar(midiaFile);
            // experimento.addMidia(midiaSalva);
        }

        // 3. Salva a entidade Experimento no banco de dados
        Experimento experimentoSalvo = experimentoRepository.save(experimento);

        // 4. Converte a entidade salva para um DTO de resposta e retorna
        return experimentoMapper.toResponseDTO(experimentoSalvo);
    }


    // SEU MÉTODO ANTIGO CONTINUA AQUI (pode ser útil no futuro ou pode ser removido)
    @Transactional
    public ExperimentoResponseDTO salvar(ExperimentoRequestDTO requestDTO) {
        Pesquisador pesquisador = pesquisadorRepository.findById(requestDTO.pesquisadorId())
                .orElseThrow(() -> new EntityNotFoundException("Pesquisador com ID " + requestDTO.pesquisadorId() + " não encontrado."));

        if (requestDTO.dataInicio().isAfter(requestDTO.dataFim())) {
            throw new IllegalArgumentException("A data de início não pode ser depois da data de fim.");
        }

        Experimento experimento = experimentoMapper.toEntity(requestDTO);
        experimento.setPesquisadorResponsavel(pesquisador);
        Experimento experimentoSalvo = experimentoRepository.save(experimento);
        return experimentoMapper.toResponseDTO(experimentoSalvo);
    }

    // O RESTO DOS SEUS MÉTODOS CONTINUAM IGUAIS
    @Transactional(readOnly = true)
    public List<ExperimentoResponseDTO> buscarTodos() {
        List<Experimento> experimentos = experimentoRepository.findAll();
        return experimentoMapper.toResponseDTOList(experimentos);
    }

    @Transactional(readOnly = true)
    public ExperimentoResponseDTO buscarPorId(Long id) {
        Experimento experimento = experimentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Experimento com ID " + id + " não encontrado."));
        return experimentoMapper.toResponseDTO(experimento);
    }

    @Transactional
    public ExperimentoResponseDTO atualizar(Long id, ExperimentoRequestDTO requestDTO) {
        Experimento experimentoExistente = experimentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Experimento com ID " + id + " não encontrado."));

        experimentoExistente.setNome(requestDTO.nome());
        experimentoExistente.setDescricao(requestDTO.descricao());
        experimentoExistente.setDataInicio(requestDTO.dataInicio());
        experimentoExistente.setDataFim(requestDTO.dataFim());

        Experimento experimentoAtualizado = experimentoRepository.save(experimentoExistente);
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
        Experimento experimento = experimentoRepository.findById(idExperimento)
                .orElseThrow(() -> new EntityNotFoundException("Experimento com ID " + idExperimento + " não encontrado."));

        Participante participante = participanteRepository.findById(idParticipante)
                .orElseThrow(() -> new EntityNotFoundException("Participante com ID " + idParticipante + " não encontrado."));

        experimento.getParticipantes().add(participante);
        experimentoRepository.save(experimento);
    }
}