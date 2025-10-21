package com.vibetrack.backend.users.DTO.mobile;

import java.util.List;

// DTO para os dados de saúde, que agrupa os demais
public class HealthDataDTO {
    private int steps;
    private List<HeartRateDTO> heartRate;

    // Getters e Setters
    public int getSteps() { return steps; }
    public void setSteps(int steps) { this.steps = steps; }
    public List<HeartRateDTO> getHeartRate() { return heartRate; }
    public void setHeartRate(List<HeartRateDTO> heartRate) { this.heartRate = heartRate; }
}