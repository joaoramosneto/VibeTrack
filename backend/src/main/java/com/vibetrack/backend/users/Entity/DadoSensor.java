package com.vibetrack.backend.users.Entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "dados_sensores")
public class DadoSensor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Instant timestamp; // O momento exato da coleta, em UTC. Essencial para séries temporais.

    @Column(nullable = false)
    private String tipo; // Ex: "FREQUENCIA_CARDIACA", "PASSOS", "NIVEL_ESTRESSE"

    @Column(nullable = false)
    private Double valor; // O valor numérico da medição

    private String unidade; // Ex: "bpm" (batimentos por minuto), "passos/min"

    // Relacionamento para saber a qual experimento este dado pertence
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "experimento_id", nullable = false)
    private Experimento experimento;

    // Relacionamento para saber de qual participante este dado veio
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participante_id", nullable = false)
    private Participante participante;

    // Getters e Setters (ou use Lombok)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public Double getValor() { return valor; }
    public void setValor(Double valor) { this.valor = valor; }
    public String getUnidade() { return unidade; }
    public void setUnidade(String unidade) { this.unidade = unidade; }
    public Experimento getExperimento() { return experimento; }
    public void setExperimento(Experimento experimento) { this.experimento = experimento; }
    public Participante getParticipante() { return participante; }
    public void setParticipante(Participante participante) { this.participante = participante; }
}