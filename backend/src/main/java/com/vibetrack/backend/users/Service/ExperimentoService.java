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
import com.vibetrack.backend.users.Entity.DadoBiometrico;
import com.vibetrack.backend.users.Repository.DadoBiometricoRepository;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList; 
import java.util.List; 
import java.util.stream.Collectors;

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
    @Autowired 
    private DadoBiometricoRepository dadoBiometricoRepository;

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

        emailService.enviarEmailConfirmacaoExperimento(
                pesquisador.getEmail(),
                pesquisador.getNome(),
                experimentoSalvo.getNome()
        );

        return experimentoMapper.toResponseDTO(experimentoSalvo);
    }

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

    // MÉTODO FINALIZADO (SEM MOCK)
    @Transactional(readOnly = true)
    public DashboardDTO getDashboardData(Long experimentoId) {
        if (!experimentoRepository.existsById(experimentoId)) {
            throw new EntityNotFoundException("Experimento com ID " + experimentoId + " não encontrado.");
        }

        // --- 1. CONFIGURAÇÃO PARA GRÁFICO DE COMPARAÇÃO DE TIPOS (Eixo X = Tipos) ---

        // Rótulos do Eixo X (Tipos de FC traduzidos para exibição)
        List<String> labelsTipos = List.of("FC Mínima", "FC Média", "FC Máxima"); 

        // Valores do Eixo Y (BPM)
        List<Integer> dadosBatimentos = new ArrayList<>();
        
        // Tipos a buscar no banco (nomes exatos da coluna TIPO_DADO)
        List<String> tipos = List.of("FC_MINIMA", "FC_MEDIA", "FC_MAXIMA");
        
        // Busca o último valor (mais recente) para cada tipo
        for (String tipo : tipos) {
            List<DadoBiometrico> dados = dadoBiometricoRepository.findByExperimentoIdAndTipoDadoOrderByTimestampAsc(
                    experimentoId,
                    tipo
            );
            
            if (!dados.isEmpty()) {
                DadoBiometrico ultimoDado = dados.get(dados.size() - 1);
                dadosBatimentos.add(ultimoDado.getValor().intValue());
            } else {
                // Adiciona 0 se o dado estiver faltando
                dadosBatimentos.add(0);
            }
        }
        
        // 2. Cria o Dataset e o DTO do gráfico (Saída correta)
        var datasetFrequencia = new LineChartDatasetDTO("Batimentos por Minuto (BPM)", dadosBatimentos);
        var graficoFrequencia = new LineChartDataDTO(labelsTipos, List.of(datasetFrequencia));

        // 3. REMOÇÃO DO MOCK: Retorna ChartDataDTO vazio
        var graficoEmocoes = new ChartDataDTO(List.of(), List.of());

        // 4. Retorna
        return new DashboardDTO(graficoFrequencia, graficoEmocoes);
    }
}
