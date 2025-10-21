package com.vibetrack.backend.users.DTO.mobile;

public class HeartRateDTO {
    private long timestamp;
    private int value;

    // Getters e Setters
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    public int getValue() { return value; }
    public void setValue(int value) { this.value = value; }
}