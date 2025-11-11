package com.vibetrack.backend.users.DTO;

// Se você usa Lombok, pode usar @Data, @Getter, @Setter

public class ResetPasswordRequestDTO {

    private String token;
    private String novaSenha;

    // --- Construtores ---
    public ResetPasswordRequestDTO() {
    }

    // --- Getters e Setters ---
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNovaSenha() {
        return novaSenha;
    }

    public void setNovaSenha(String novaSenha) {
        this.novaSenha = novaSenha;
    }
}