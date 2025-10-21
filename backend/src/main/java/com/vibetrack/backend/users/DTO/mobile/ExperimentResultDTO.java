package com.vibetrack.backend.users.DTO.mobile;

public class ExperimentResultDTO {
    private String userId;
    private String deviceData;
    private HealthDataDTO healthData;

    // Getters e Setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getDeviceData() { return deviceData; }
    public void setDeviceData(String deviceData) { this.deviceData = deviceData; }
    public HealthDataDTO getHealthData() { return healthData; }
    public void setHealthData(HealthDataDTO healthData) { this.healthData = healthData; }
}