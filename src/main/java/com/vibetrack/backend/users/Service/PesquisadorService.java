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

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PesquisadorService {

    @Autowired
    private PesquisadorRepository pesquisadorRepository;

    @Autowired
    private PesquisadorMapper pesquisadorMapper;

    @Autowired
    private PasswordEncoder passwordEncoder; // Injeta o codificador de senhas

    @Transactional
    public PesquisadorResponseDTO criarPesquisador(PesquisadorRequestDTO requestDTO) {
        // Converte o DTO para a entidade
        Pesquisador pesquisador = pesquisadorMapper.toEntity(requestDTO);

        // CRIPTOGRAFA A SENHA ANTES DE SALVAR
        pesquisador.setSenha(passwordEncoder.encode(requestDTO.senha()));

        // Salva a entidade no banco
        Pesquisador novoPesquisador = pesquisadorRepository.save(pesquisador);

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

    // Métodos para atualizar e deletar podem ser adicionados aqui seguindo o mesmo padrão...
}
