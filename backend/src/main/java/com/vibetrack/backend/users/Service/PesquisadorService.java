package com.vibetrack.backend.users.Service;

// Import do DTO que criámos para a troca de senha
import com.vibetrack.backend.users.DTO.pesquisadorDTO.ChangePasswordRequestDTO;
import com.vibetrack.backend.users.DTO.pesquisadorDTO.PesquisadorRequestDTO;
import com.vibetrack.backend.users.DTO.pesquisadorDTO.PesquisadorResponseDTO;
import com.vibetrack.backend.users.Entity.Pesquisador;
import com.vibetrack.backend.users.Mapper.PesquisadorMapper;
import com.vibetrack.backend.users.Repository.PesquisadorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PesquisadorService {

    private final FileStorageService fileStorageService;

    @Autowired
    private PesquisadorRepository pesquisadorRepository;

    @Autowired
    private PesquisadorMapper pesquisadorMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    public PesquisadorService(PesquisadorRepository pesquisadorRepository, FileStorageService fileStorageService) {
        this.pesquisadorRepository = pesquisadorRepository;
        this.fileStorageService = fileStorageService;
    }

    public void atualizarFotoPerfil(Pesquisador pesquisadorLogado, MultipartFile file) {
        String nomeDoArquivo = fileStorageService.storeFile(file);
        pesquisadorLogado.setFotoUrl(nomeDoArquivo);
        pesquisadorRepository.save(pesquisadorLogado);
    }

    @Transactional
    public PesquisadorResponseDTO criarPesquisador(PesquisadorRequestDTO requestDTO) {
        if (pesquisadorRepository.findByEmail(requestDTO.email()) != null) {
            throw new IllegalArgumentException("Este email já está em uso.");
        }

        Pesquisador pesquisador = pesquisadorMapper.toEntity(requestDTO);
        pesquisador.setSenha(passwordEncoder.encode(requestDTO.senha()));

        // Modo Direto: Ativo = true
        pesquisador.setAtivo(true);
        pesquisador.setCodigoVerificacao(null);
        pesquisador.setCodigoVerificacaoExpiracao(null);

        Pesquisador novoPesquisador = pesquisadorRepository.save(pesquisador);
        System.out.println(">>> USUÁRIO ATIVO CRIADO: " + novoPesquisador.getEmail());

        return pesquisadorMapper.toResponseDTO(novoPesquisador);
    }

    @Transactional(readOnly = true)
    public List<PesquisadorResponseDTO> listarTodosPesquisadores() {
        return pesquisadorRepository.findAll()
                .stream()
                .map(pesquisadorMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PesquisadorResponseDTO buscarPesquisadorPorId(Long id) {
        Pesquisador pesquisador = pesquisadorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pesquisador com ID " + id + " não encontrado."));
        return pesquisadorMapper.toResponseDTO(pesquisador);
    }

    @Transactional
    public void verificarCodigoEAtivarConta(String codigo) {
        Pesquisador pesquisador = pesquisadorRepository.findByCodigoVerificacao(codigo)
                .orElseThrow(() -> new EntityNotFoundException("Código inválido."));

        pesquisador.setAtivo(true);
        pesquisador.setCodigoVerificacao(null);
        pesquisadorRepository.save(pesquisador);
    }

    // VVVV NOVO MÉTODO: ALTERAR SENHA (PERFIL) VVVV
    @Transactional
    public void alterarSenha(Long pesquisadorId, ChangePasswordRequestDTO requestDTO) {
        // 1. Valida se as senhas novas batem
        if (!requestDTO.novaSenha().equals(requestDTO.confirmacaoSenha())) {
            throw new IllegalArgumentException("A nova senha e a confirmação não coincidem.");
        }

        // 2. Busca o pesquisador
        Pesquisador pesquisador = pesquisadorRepository.findById(pesquisadorId)
                .orElseThrow(() -> new EntityNotFoundException("Pesquisador não encontrado."));

        // 3. Verifica se a senha ATUAL está correta
        if (!passwordEncoder.matches(requestDTO.senhaAtual(), pesquisador.getSenha())) {
            throw new IllegalArgumentException("A senha atual está incorreta.");
        }

        // 4. Salva a nova senha criptografada
        pesquisador.setSenha(passwordEncoder.encode(requestDTO.novaSenha()));
        pesquisadorRepository.save(pesquisador);
    }
    // ^^^^ FIM DO NOVO MÉTODO ^^^^
}