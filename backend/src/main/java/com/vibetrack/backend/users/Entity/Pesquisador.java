package com.vibetrack.backend.users.Entity;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime; // Este import já existia
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "pesquisadores")
public class Pesquisador implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Column(name = "foto_url") // Define o nome da coluna no banco de dados
    private String fotoUrl;

    // vvv CAMPOS NOVOS PARA VERIFICAÇÃO DE EMAIL vvv
    private boolean ativo;
    private String codigoVerificacao;
    private LocalDateTime codigoVerificacaoExpiracao;
    // ^^^ FIM DOS CAMPOS NOVOS ^^^

    // vvvv CAMPOS NOVOS PARA RESET DE SENHA vvvv
    @Column(name = "reset_token")
    private String resetToken;

    @Column(name = "reset_token_expiry")
    private LocalDateTime resetTokenExpiry;
    // ^^^^ FIM DOS CAMPOS NOVOS PARA RESET DE SENHA ^^^^


    // Construtores
    public Pesquisador() {
    }

    public Pesquisador(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.ativo = false; // MODIFICADO: Todo novo pesquisador começa como inativo
    }

    // Getters e Setters (incluindo os novos)
    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    // vvv GETTERS E SETTERS PARA OS NOVOS CAMPOS vvv
    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public String getCodigoVerificacao() {
        return codigoVerificacao;
    }

    public void setCodigoVerificacao(String codigoVerificacao) {
        this.codigoVerificacao = codigoVerificacao;
    }

    public LocalDateTime getCodigoVerificacaoExpiracao() {
        return codigoVerificacaoExpiracao;
    }

    public void setCodigoVerificacaoExpiracao(LocalDateTime codigoVerificacaoExpiracao) {
        this.codigoVerificacaoExpiracao = codigoVerificacaoExpiracao;
    }
    // ^^^ FIM DOS GETTERS E SETTERS NOVOS ^^^

    // vvvv GETTERS E SETTERS PARA RESET DE SENHA vvvv
    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public LocalDateTime getResetTokenExpiry() {
        return resetTokenExpiry;
    }

    public void setResetTokenExpiry(LocalDateTime resetTokenExpiry) {
        this.resetTokenExpiry = resetTokenExpiry;
    }
    // ^^^^ FIM DOS GETTERS E SETTERS PARA RESET DE SENHA ^^^^


    // --- MÉTODOS DO UserDetails ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Por enquanto, todo pesquisador terá o papel "ROLE_USER".
        // Futuramente, você pode carregar isso de uma tabela de Papeis/Roles.
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }



    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        // MODIFICAÇÃO MAIS IMPORTANTE:
        // Agora, o Spring Security vai considerar a conta ativa
        // apenas se o nosso campo 'ativo' for 'true'.
        return this.ativo;
    }
}