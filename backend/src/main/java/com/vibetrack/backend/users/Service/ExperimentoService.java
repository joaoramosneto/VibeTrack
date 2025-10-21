package com.vibetrack.backend.users.Service;

import com.vibetrack.backend.users.DTO.DashboardDTO.ChartDataDTO;
import com.vibetrack.backend.users.DTO.DashboardDTO.DashboardDTO;
import com.vibetrack.backend.users.DTO.DashboardDTO.LineChartDataDTO;
import com.vibetrack.backend.users.DTO.DashboardDTO.LineChartDatasetDTO;
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

    // vvvv PASSO 1: INJETAR O SERVIÇO DE EMAIL vvvv
    @Autowired
    private EmailService emailService;

    @Transactional
    public ExperimentoResponseDTO salvarComMidia(ExperimentoRequestDTO requestDTO, MultipartFile midiaFile) {
        Pesquisador pesquisador = pesquisadorRepository.findById(requestDTO.pesquisadorId())
                .orElseThrow(() -> new EntityNotFoundException("Pesquisador com ID " + requestDTO.pesquisadorId() + " não encontrado."));

        if (requestDTO.dataInicio().isAfter(requestDTO.dataFim())) {
            throw new IllegalArgumentException("A data de início não pode ser depois da data de fim.");
        }

        Experimento experimento = experimentoMapper.toEntity(requestDTO);
        experimento.setPesquisadorResponsavel(pesquisador);

        if (midiaFile != null && !midiaFile.isEmpty()) {
            System.out.println("Arquivo de mídia recebido: " + midiaFile.getOriginalFilename());
            System.out.println("Tamanho do arquivo: " + midiaFile.getSize() + " bytes");
            // TODO: Implementar a lógica real de salvamento do arquivo
        }

        Experimento experimentoSalvo = experimentoRepository.save(experimento);

        // vvvv PASSO 2: CHAMAR O SERVIÇO DE EMAIL AQUI vvvv
        // Dispara o email de confirmação em segundo plano
        emailService.enviarEmailConfirmacaoExperimento(
                pesquisador.getEmail(),
                pesquisador.getNome(),
                experimentoSalvo.getNome()
        );
        // ^^^^ FIM DA CHAMADA ^^^^

        return experimentoMapper.toResponseDTO(experimentoSalvo);
    }

    // O resto dos seus métodos continuam exatamente como estavam antes
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

        // Também vamos adicionar o envio de email aqui, para o caso de este método ser usado
        emailService.enviarEmailConfirmacaoExperimento(
                pesquisador.getEmail(),
                pesquisador.getNome(),
                experimentoSalvo.getNome()
        );

        return experimentoMapper.toResponseDTO(experimentoSalvo);
    }

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
        experimentoExistente.setStatusExperimento(StatusExperimento.valueOf(requestDTO.statusExperimento()));

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

    public DashboardDTO getDashboardDataMock(Long experimentoId) {
        // Simula que estamos buscando um experimento
        // Experimento experimento = experimentoRepository.findById(experimentoId).orElseThrow(...);

        // Mock para o gráfico de Frequência Cardíaca (Linha)
        var labelsTempo = List.of("0s", "10s", "20s", "30s", "40s", "50s", "60s");
        var dadosBatimentos = List.of(72, 75, 74, 80, 85, 82, 78);
        var datasetFrequencia = new LineChartDatasetDTO("Frequência Cardíaca (BPM)", dadosBatimentos);
        var graficoFrequencia = new LineChartDataDTO(labelsTempo, List.of(datasetFrequencia));

        // Mock para o gráfico de Distribuição de Emoções (Pizza)
        var labelsEmocoes = List.of("Neutro", "Feliz", "Surpreso", "Triste");
        var dadosContagem = List.of(150, 90, 45, 15); // Simula a contagem de frames/eventos
        var graficoEmocoes = new ChartDataDTO(labelsEmocoes, dadosContagem);

        // Junta tudo no DTO principal e retorna
        return new DashboardDTO(graficoFrequencia, graficoEmocoes);
    }
}