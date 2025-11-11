package com.vibetrack.backend.users.DTO;

// Se você usa Lombok no seu backend, pode simplesmente adicionar @Data, @Getter, @Setter

public class ForgotPasswordRequestDTO {

    private String email;

    // --- Construtores ---
    public ForgotPasswordRequestDTO() {
        // Construtor vazio padrão
    }

    public ForgotPasswordRequestDTO(String email) {
        this.email = email;
    }

    // --- Getters e Setters ---
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}