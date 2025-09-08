package com.vibetrack.backend.Security;

import com.vibetrack.backend.users.Repository.PesquisadorRepository;
import com.vibetrack.backend.users.Service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component // Marca esta classe como um componente gerenciado pelo Spring
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PesquisadorRepository pesquisadorRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = this.recuperarToken(request);

        if (token != null) {
            var email = tokenService.validarToken(token); // Valida o token e pega o email (subject)
            UserDetails pesquisador = pesquisadorRepository.findByEmail(email);

            if (pesquisador != null) {
                // Se o usuário existe, nós o autenticamos no Spring Security
                var authentication = new UsernamePasswordAuthenticationToken(pesquisador, null, pesquisador.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // Continua o fluxo da requisição
        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            return null;
        }
        // O token vem depois de "Bearer "
        return authHeader.replace("Bearer ", "");
    }
}