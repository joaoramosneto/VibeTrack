package com.vibetrack.backend.users.Service; // Ou o seu pacote de serviços

import com.vibetrack.backend.users.DTO.session.*;
import com.vibetrack.backend.users.Entity.Experimento;
import com.vibetrack.backend.users.Entity.Participante;
import com.vibetrack.backend.users.Entity.SessaoColeta;
import com.vibetrack.backend.users.Repository.ExperimentoRepository;
import com.vibetrack.backend.users.Repository.ParticipanteRepository;
import com.vibetrack.backend.users.Repository.SessaoColetaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class SessaoColetaService {

    @Autowired private SessaoColetaRepository sessaoColetaRepository;
    @Autowired private ExperimentoRepository experimentoRepository;
    @Autowired private ParticipanteRepository participanteRepository;

    @Transactional
    public SessaoColetaResponseDTO criarSessao(SessaoColetaRequestDTO request) {
        Experimento experimento = experimentoRepository.findById(request.experimentoId())
                .orElseThrow(() -> new EntityNotFoundException("Experimento não encontrado."));
        Participante participante = participanteRepository.findById(request.participanteId())
                .orElseThrow(() -> new EntityNotFoundException("Participante não encontrado."));

        SessaoColeta sessao = new SessaoColeta();
        sessao.setExperimento(experimento);
        sessao.setParticipante(participante);
        sessao.setDataCriacao(LocalDateTime.now());
        sessao.setDataExpiracao(LocalDateTime.now().plusMinutes(5)); // Válido por 5 minutos
        sessao.setCodigo(gerarCodigoUnico());
        sessao.setAtiva(true);

        sessaoColetaRepository.save(sessao);

        return new SessaoColetaResponseDTO(sessao.getCodigo(), sessao.getDataExpiracao());
    }

    @Transactional
    public ValidacaoSessaoResponseDTO validarSessao(ValidacaoSessaoRequestDTO request) {
        SessaoColeta sessao = sessaoColetaRepository.findByCodigoAndAtivaTrue(request.codigo())
                .orElseThrow(() -> new EntityNotFoundException("Código de sessão inválido ou expirado."));

        if (LocalDateTime.now().isAfter(sessao.getDataExpiracao())) {
            sessao.setAtiva(false);
            sessaoColetaRepository.save(sessao);
            throw new IllegalStateException("Código de sessão expirado.");
        }

        // Desativa a sessão após o primeiro uso para segurança
        sessao.setAtiva(false);
        sessaoColetaRepository.save(sessao);

        return new ValidacaoSessaoResponseDTO(
                sessao.getExperimento().getId(),
                sessao.getParticipante().getId()
        );
    }

    private String gerarCodigoUnico() {
        // Gera um código simples de 6 caracteres
        return UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }
}