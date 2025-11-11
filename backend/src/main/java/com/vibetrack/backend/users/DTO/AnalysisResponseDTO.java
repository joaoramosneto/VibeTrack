package com.vibetrack.backend.users.DTO;

public class AnalysisResponseDTO {

    private String emocaoDetectada;

    // Construtor
    public AnalysisResponseDTO(String emocaoDetectada) {
        this.emocaoDetectada = emocaoDetectada;
    }

    // Getter
    public String getEmocaoDetectada() {
        return emocaoDetectada;
    }

    // Setter
    public void setEmocaoDetectada(String emocaoDetectada) {
        this.emocaoDetectada = emocaoDetectada;
    }
}