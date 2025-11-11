package com.vibetrack.backend.users.DTO;

// Se você usa Lombok, pode usar @Data, @Getter, @Setter

public class BpmRequestDTO {

    private double bpm; // O campo que o frontend vai enviar

    // Construtor vazio
    public BpmRequestDTO() {
    }

    // Getter
    public double getBpm() {
        return bpm;
    }

    // Setter
    public void setBpm(double bpm) {
        this.bpm = bpm;
    }
}