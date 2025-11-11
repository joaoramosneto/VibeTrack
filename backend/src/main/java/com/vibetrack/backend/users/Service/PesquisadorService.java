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
import java.util.UUID;

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

    @Transactional
    public void iniciarResetSenha(String email) {
        // 1. Procura o pesquisador pelo e-mail
        Pesquisador pesquisador = pesquisadorRepository.findByEmail(email);

        // 2. VERIFICAÇÃO DE SEGURANÇA:
        // Se o pesquisador não existir OU não estiver ativo,
        // nós NÃO fazemos nada. Nós não queremos que um hacker
        // descubra quais e-mails estão cadastrados ou inativos.
        // Nós simplesmente fingimos que o processo continua.
        if (pesquisador == null || !pesquisador.isAtivo()) {
            // Log silencioso (opcional, bom para debug)
            System.out.println("Solicitação de reset para e-mail não encontrado ou inativo: " + email);
            return; // Sai do método silenciosamente.
        }

        // 3. Se o pesquisador foi encontrado E está ativo:
        try {
            // Gerar um token secreto e aleatório
            String token = UUID.randomUUID().toString();

            // Definir a expiração (ex: 1 hora a partir de agora)
            LocalDateTime expiryDate = LocalDateTime.now().plusHours(1);

            // 4. Salvar o token e a expiração no usuário
            pesquisador.setResetToken(token);
            pesquisador.setResetTokenExpiry(expiryDate);
            pesquisadorRepository.save(pesquisador);

            // 5. Enviar o e-mail de verdade
            // (Vamos criar este método 'enviarEmailDeReset' no EmailService no próximo passo)
            emailService.enviarEmailDeReset(pesquisador.getEmail(), pesquisador.getNome(), token);

        } catch (Exception e) {
            // Se o envio de e-mail falhar, não queremos que o usuário saiba.
            // Apenas logamos o erro no backend.
            System.err.println("Falha ao enviar e-mail de reset para: " + email);
            e.printStackTrace();
        }
    }

    // Em: PesquisadorService.java

    // ... (aqui estão seus outros métodos como iniciarResetSenha)

    /**
     * Finaliza o processo de reset de senha.
     * 1. Valida o token.
     * 2. Verifica a expiração.
     * 3. Criptografa e salva a nova senha.
     * 4. Limpa o token para segurança.
     */
    // Em: src/main/java/com/vibetrack/backend/users/Service/PesquisadorService.java

    @Transactional
    public void finalizarResetSenha(String token, String novaSenha) {
        // 1. Procura o pesquisador pelo token
        Pesquisador pesquisador = pesquisadorRepository.findByResetToken(token)
                .orElseThrow(() -> new EntityNotFoundException("Token de redefinição inválido.")); // Erro se o token não existir

        // 2. Verifica se o token já expirou
        if (pesquisador.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            // Limpa o token expirado
            pesquisador.setResetToken(null);
            pesquisador.setResetTokenExpiry(null);
            pesquisadorRepository.save(pesquisador); // < AQUI 'save' está OK, é só limpeza

            throw new IllegalArgumentException("Token de redefinição expirado. Por favor, solicite um novo.");
        }

        // 3. Se o token for válido e não expirou:
        // Criptografa a nova senha (usando o PasswordEncoder que já temos)
        pesquisador.setSenha(passwordEncoder.encode(novaSenha));

        // 4. Limpa os campos de token (MUITO IMPORTANTE para segurança!)
        pesquisador.setResetToken(null);
        pesquisador.setResetTokenExpiry(null);

        // 5. Salva o usuário com a nova senha e os tokens limpos
        //    USANDO saveAndFlush() PARA FORÇAR A ESCRITA IMEDIATA NO BANCO
        pesquisadorRepository.saveAndFlush(pesquisador); // <-- A MUDANÇA ESTÁ AQUI
    }
}