package com.vibetrack.backend.users.Service;

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

import java.time.LocalDateTime; // NOVO IMPORT
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
        // 1. Delega o armazenamento do arquivo para o FileStorageService
        String nomeDoArquivo = fileStorageService.storeFile(file);

        // 2. Atualiza a entidade com a nova URL/nome da foto
        pesquisadorLogado.setFotoUrl(nomeDoArquivo);

        // 3. Salva a entidade atualizada no banco de dados através do repositório
        pesquisadorRepository.save(pesquisadorLogado);
    }

    @Transactional
    public PesquisadorResponseDTO criarPesquisador(PesquisadorRequestDTO requestDTO) {
        // Verificação para ver se o email já existe
        // Apenas mudamos a forma de verificar se o pesquisador foi encontrado
        if (pesquisadorRepository.findByEmail(requestDTO.email()) != null) {
            throw new IllegalArgumentException("Este email já está em uso.");
        }

        // Converte o DTO para a entidade
        Pesquisador pesquisador = pesquisadorMapper.toEntity(requestDTO);

        // Criptografa a senha antes de salvar
        pesquisador.setSenha(passwordEncoder.encode(requestDTO.senha()));

        // --- LÓGICA NOVA PARA VERIFICAÇÃO ---
        // 1. Gera um código de 6 dígitos aleatório
        String codigo = String.format("%06d", new java.util.Random().nextInt(999999));

        // 2. Define o código e a data de expiração (ex: 15 minutos a partir de agora)
        pesquisador.setCodigoVerificacao(codigo);
        pesquisador.setCodigoVerificacaoExpiracao(LocalDateTime.now().plusMinutes(15));
        pesquisador.setAtivo(false); // O usuário começa como inativo
        // --- FIM DA LÓGICA NOVA ---

        // Salva a entidade no banco
        Pesquisador novoPesquisador = pesquisadorRepository.save(pesquisador);

        // Dispara o email com o código de verificação
        // (Vamos criar este método no EmailService no próximo passo)
        emailService.enviarEmailDeVerificacao(novoPesquisador.getEmail(), novoPesquisador.getNome(), codigo);

        // Retorna o DTO de resposta
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

    //    ↓↓↓ ADICIONE ESTE MÉTODO NO SEU PesquisadorService.java ↓↓↓

    @Transactional
    public void verificarCodigoEAtivarConta(String codigo) {
        // 1. Procura um pesquisador que tenha este código de verificação
        Pesquisador pesquisador = pesquisadorRepository.findByCodigoVerificacao(codigo)
                .orElseThrow(() -> new EntityNotFoundException("Código de verificação inválido."));

        // 2. Verifica se o código já expirou
        if (pesquisador.getCodigoVerificacaoExpiracao().isBefore(java.time.LocalDateTime.now())) {
            // Opcional: aqui você poderia gerar um novo código e reenviar o email
            throw new IllegalArgumentException("Código de verificação expirado. Por favor, solicite um novo.");
        }

        // 3. Se o código é válido e não expirou, ativa a conta
        pesquisador.setAtivo(true);
        // Limpa os campos de verificação para segurança
        pesquisador.setCodigoVerificacao(null);
        pesquisador.setCodigoVerificacaoExpiracao(null);

        // 4. Salva as alterações no banco de dados
        pesquisadorRepository.save(pesquisador);
    }
}