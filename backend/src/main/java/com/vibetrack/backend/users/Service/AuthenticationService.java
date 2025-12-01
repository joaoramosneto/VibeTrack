package com.vibetrack.backend.users.Service;

import com.vibetrack.backend.users.Entity.Pesquisador;
import com.vibetrack.backend.users.Repository.PesquisadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements UserDetailsService {

    @Autowired
    private PesquisadorRepository pesquisadorRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Pesquisador pesquisador = pesquisadorRepository.findByEmail(username);

        // VVVV DEBUG ADICIONADO AQUI VVVV
        if (pesquisador == null) {
            System.out.println(">>> DEBUG AUTH: Usuário não encontrado no banco: " + username);
            throw new UsernameNotFoundException("Usuário não encontrado: " + username);
        }
        System.out.println(">>> DEBUG AUTH: Usuário encontrado. Status Ativo: " + pesquisador.isAtivo());
        // ^^^^ DEBUG ADICIONADO AQUI ^^^^

        return pesquisador;
    }
}