package com.vibetrack.backend.users.Service;

import com.vibetrack.backend.users.DTO.DashboardDTO.ChartDataDTO;
import com.vibetrack.backend.users.DTO.DashboardDTO.DashboardDTO;
import com.vibetrack.backend.users.DTO.DashboardDTO.LineChartDataDTO;
import com.vibetrack.backend.users.DTO.DashboardDTO.LineChartDatasetDTO;
import com.vibetrack.backend.users.DTO.ExperimentoRequestDTO;
import com.vibetrack.backend.users.DTO.ExperimentoResponseDTO;
import com.vibetrack.backend.users.Entity.Enums.StatusExperimento;
import com.vibetrack.backend.users.Entity.Experimento;
import com.vibetrack.backend.users.Entity.Midia;
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

import java.io.IOException;
import java.util.ArrayList;
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

    @Autowired
    private EmailService emailService;

    @Transactional
    public ExperimentoResponseDTO salvarComMidia(ExperimentoRequestDTO requestDTO, List<MultipartFile> midiaFiles) {
        Pesquisador pesquisador = pesquisadorRepository.findById(requestDTO.pesquisadorId())
                .orElseThrow(() -> new EntityNotFoundException("Pesquisador com ID " + requestDTO.pesquisadorId() + " não encontrado."));

        if (requestDTO.dataInicio().isAfter(requestDTO.dataFim())) {
            throw new IllegalArgumentException("A data de início não pode ser depois da data de fim.");
        }

        // VVVV LÓGICA DE BUSCA E ASSOCIAÇÃO DO PARTICIPANTE VVVV
        Participante participante = null;
        if (requestDTO.participanteId() != null) {
            participante = participanteRepository.findById(requestDTO.participanteId())
                    .orElseThrow(() -> new EntityNotFoundException("Participante com ID " + requestDTO.participanteId() + " não encontrado."));
        }

        Experimento experimento = experimentoMapper.toEntity(requestDTO);
        experimento.setPesquisadorResponsavel(pesquisador);
        experimento.setParticipantePrincipal(participante); // <-- Associa o participante principal
        // ^^^^ FIM DA LÓGICA DE BUSCA ^^^^

        // Lógica BLOB (Salvar no Banco)
        if (midiaFiles != null && !midiaFiles.isEmpty()) {
            for (MultipartFile file : midiaFiles) {
                if (!file.isEmpty()) {
                    try {
                        Midia midia = new Midia(file.getOriginalFilename(), file.getContentType(), file.getBytes());
                        experimento.getMidias().add(midia);
                    } catch (IOException e) {
                        throw new RuntimeException("Erro ao processar arquivo de mídia", e);
                    }
                }
            }
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
    public ExperimentoResponseDTO atualizarMidia(Long id, List<MultipartFile> midiaFiles) {
        Experimento experimento = experimentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Experimento com ID " + id + " não encontrado."));

        experimento.getMidias().clear(); // Limpa as mídias antigas

        if (midiaFiles != null && !midiaFiles.isEmpty()) {
            for (MultipartFile file : midiaFiles) {
                if (!file.isEmpty()) {
                    try {
                        Midia midia = new Midia(file.getOriginalFilename(), file.getContentType(), file.getBytes());
                        experimento.getMidias().add(midia);
                    } catch (IOException e) {
                        throw new RuntimeException("Erro ao processar arquivo de mídia na atualização", e);
                    }
                }
            }
        }

        Experimento experimentoAtualizado = experimentoRepository.save(experimento);
        return experimentoMapper.toResponseDTO(experimentoAtualizado);
    }

    @Transactional
    public ExperimentoResponseDTO salvar(ExperimentoRequestDTO requestDTO) {
        return salvarComMidia(requestDTO, null); // Reutiliza o método principal
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

        // VVVV LÓGICA DE BUSCA E ASSOCIAÇÃO DO PARTICIPANTE (Para o UPDATE) VVVV
        Participante participante = null;
        if (requestDTO.participanteId() != null) {
            participante = participanteRepository.findById(requestDTO.participanteId())
                    .orElseThrow(() -> new EntityNotFoundException("Participante com ID " + requestDTO.participanteId() + " não encontrado."));
        }
        experimentoExistente.setParticipantePrincipal(participante); // <-- Associa o participante principal
        // ^^^^ FIM DA LÓGICA DE BUSCA ^^^^

        experimentoExistente.setNome(requestDTO.nome());
        experimentoExistente.setDescricaoGeral(requestDTO.descricaoAmbiente());
        experimentoExistente.setResultadoEmocional(requestDTO.tipoEmocao());
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
        if (!experimentoRepository.existsById(experimentoId)) {
            throw new EntityNotFoundException("Experimento com ID " + experimentoId + " não encontrado.");
        }

        List<String> labelsTipos = List.of("FC Mínima", "FC Média", "FC Máxima");
        List<Integer> dadosBatimentos = new ArrayList<>();
        List<String> tipos = List.of("FC_MINIMA", "FC_MEDIA", "FC_MAXIMA");

        for (String tipo : tipos) {
            List<DadoBiometrico> dados = dadoBiometricoRepository.findByExperimentoIdAndTipoDadoOrderByTimestampAsc(
                    experimentoId,
                    tipo
            );
            if (!dados.isEmpty()) {
                DadoBiometrico ultimoDado = dados.get(dados.size() - 1);
                dadosBatimentos.add(ultimoDado.getValor().intValue());
            } else {
                dadosBatimentos.add(0);
            }
        }
        var datasetFrequencia = new LineChartDatasetDTO("Batimentos por Minuto (BPM)", dadosBatimentos);
        var graficoFrequencia = new LineChartDataDTO(labelsTipos, List.of(datasetFrequencia));
        var graficoEmocoes = new ChartDataDTO(List.of(), List.of());

        return new DashboardDTO(graficoFrequencia, graficoEmocoes);
    }
}