package com.vibetrack.backend.users.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "dados_biometricos")
public class DadoBiometrico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime timestamp; // Horário da medição

    @Column(nullable = false)
    private String tipoDado; // "FREQUENCIA_CARDIACA" ou "PASSOS"

    @Column(nullable = false)
    private Double valor; // O valor numérico do dado (ex: 85.0 para bpm)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participante_id", nullable = false)
    private Participante participante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "experimento_id", nullable = false)
    private Experimento experimento;

    // Getters e Setters

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Experimento getExperimento() {
        return experimento;
    }

    public void setExperimento(Experimento experimento) {
        this.experimento = experimento;
    }

    public String getTipoDado() {
        return tipoDado;
    }

    public void setTipoDado(String tipoDado) {
        this.tipoDado = tipoDado;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Participante getParticipante() {
        return participante;
    }

    public void setParticipante(Participante participante) {
        this.participante = participante;
    }
    // ...
}