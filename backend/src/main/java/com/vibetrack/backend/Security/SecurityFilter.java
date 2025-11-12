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
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

    String path = request.getRequestURI();

            // Normaliza o path removendo a barra final, se ela existir
    if (path.endsWith("/") && path.length() > 1) {
        path = path.substring(0, path.length() - 1);
    }

    // Ignora endpoints públicos 
    if (path.startsWith("/api/auth/") || 
        path.equals("/api/pesquisadores") || 
        path.equals("/results") ||
        path.startsWith("/h2-console")) {

        filterChain.doFilter(request, response);
        return;
    }

    // Demais rotas exigem validação do token
    var token = recuperarToken(request);

    if (token != null) {
        try {
            var email = tokenService.validarToken(token); // Pega o subject (email)
            UserDetails pesquisador = pesquisadorRepository.findByEmail(email);

            if (pesquisador != null) {
                var authentication = new UsernamePasswordAuthenticationToken(
                        pesquisador, null, pesquisador.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            // Token inválido → 403
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
    }

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
