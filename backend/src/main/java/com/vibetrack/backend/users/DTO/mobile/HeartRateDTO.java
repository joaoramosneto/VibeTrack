package com.vibetrack.backend.users.DTO.mobile;

public class HeartRateDTO {

    // Estes campos correspondem ao JSON enviado pelo app mobile
    private int resting;
    private int average;
    private int max;

    // --- Getters e Setters Manuais ---

    public int getResting() {
        return resting;
    }

    public void setResting(int resting) {
        this.resting = resting;
    }

    public int getAverage() {
        return average;
    }

    public void setAverage(int average) {
        this.average = average;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }
}