package com.vibetrack.backend.users.DTO.mobile; // Pacote original

import lombok.Data;
import java.util.List; // Vamos remover este import

// Importe o DTO de HeartRate
import com.vibetrack.backend.users.DTO.mobile.HeartRateDTO;

@Data
public class HealthDataDTO {
    private int steps;

    // A MUDANÇA ESTÁ AQUI:
    // Trocamos List<HeartRateDTO> por apenas HeartRateDTO
    private HeartRateDTO heartRate;

    // Getters e Setters (Lombok @Data faz isso, mas se não usar Lombok,
    // você precisa mudar os métodos abaixo)

    // public List<HeartRateDTO> getHeartRate() {
    //    return heartRate;
    // }
    //
    // public void setHeartRate(List<HeartRateDTO> heartRate) {
    //    this.heartRate = heartRate;
    // }

    // MUDANÇA PARA:
    public HeartRateDTO getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(HeartRateDTO heartRate) {
        this.heartRate = heartRate;
    }

    // O getter e setter de 'steps'
    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }
}