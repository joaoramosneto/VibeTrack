package com.vibetrack.backend.users.DTO.mobile;

// import lombok.Data; // Removido
import java.util.List; // Removido

// @Data // Removido
public class ExperimentResultDTO {

    private String userId;
    private String device;
    private String date;

    // --- CORREÇÃO PRINCIPAL AQUI ---
    // Trocado de List<HealthDataDTO> para um único objeto
    private HealthDataDTO healthData;

    // --- Getters e Setters Manuais ---

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    // Getter e Setter atualizados para o objeto singular
    public HealthDataDTO getHealthData() {
        return healthData;
    }

    public void setHealthData(HealthDataDTO healthData) {
        this.healthData = healthData;
    }
}