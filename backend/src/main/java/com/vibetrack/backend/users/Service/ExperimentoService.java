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
    @Autowired 
    private DadoBiometricoRepository dadoBiometricoRepository;

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

    @Transactional(readOnly = true)
    public DashboardDTO getDashboardData(Long experimentoId) {
        // 1. Validar a existência do Experimento
        if (!experimentoRepository.existsById(experimentoId)) {
            throw new EntityNotFoundException("Experimento com ID " + experimentoId + " não encontrado.");
        }

        // 2. Buscar os dados de Frequência Cardíaca no banco
        // Utilizamos a constante 'FC_MEDIA' como exemplo de TIPO_DADO a ser buscado, baseando-se na imagem H2.
        // Se a aplicação usa 'FREQUENCIA_CARDIACA' para o dado em si, é só trocar a string.
        String TIPO_DADO_BUSCA = "FC_MEDIA"; // Usando FC_MEDIA como exemplo da estrutura da sua tabela
        List<DadoBiometrico> dadosHeartRate = dadoBiometricoRepository.findByExperimentoIdAndTipoDadoOrderByTimestampAsc(
                experimentoId,
                TIPO_DADO_BUSCA
        );

        // 3. Processar os dados para o formato LineChartDataDTO
        List<String> labelsTempo = dadosHeartRate.stream()
                // Formata o timestamp (ex: HH:mm:ss) para o eixo X
                .map(dado -> dado.getTimestamp().format(DateTimeFormatter.ofPattern("HH:mm:ss")))
                .collect(Collectors.toList());

        List<Integer> dadosBatimentos = dadosHeartRate.stream()
                .map(dado -> dado.getValor().intValue()) // Converte Double para Int para o gráfico
                .collect(Collectors.toList());

        // Cria o Dataset e o DTO do gráfico de linha
        var datasetFrequencia = new LineChartDatasetDTO("Frequência Cardíaca Média (BPM)", dadosBatimentos);
        var graficoFrequencia = new LineChartDataDTO(labelsTempo, List.of(datasetFrequencia));

        // 4. MANTÉM O MOCK para Distribuição de Emoções (Assumindo que estes dados vêm de outro lugar)
        var labelsEmocoes = List.of("Neutro", "Feliz", "Surpreso", "Triste");
        var dadosContagem = List.of(150, 90, 45, 15);
        var graficoEmocoes = new ChartDataDTO(labelsEmocoes, dadosContagem);

        // 5. Junta tudo no DTO principal e retorna
        return new DashboardDTO(graficoFrequencia, graficoEmocoes);
    }
}