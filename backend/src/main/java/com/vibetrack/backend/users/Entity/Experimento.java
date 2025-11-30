package com.vibetrack.backend.users.Entity;

import com.vibetrack.backend.users.Entity.Enums.StatusExperimento;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList; // Import necessário
import java.util.HashSet;
import java.util.List;      // Import necessário
import java.util.Set;

@Entity
@Table(name = "experimentos")
public class Experimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Lob
    @Column(columnDefinition = "TEXT", name = "descricao_geral")
    private String descricaoGeral;

    @Column(name = "resultado_emocional")
    private String resultadoEmocional;

    // VVVV MUDANÇA: DE STRING PARA LISTA DE STRINGS VVVV
    @ElementCollection
    @CollectionTable(name = "experimento_midias", joinColumns = @JoinColumn(name = "experimento_id"))
    @Column(name = "url_midia")
    private List<String> urlsMidia = new ArrayList<>();
    // ^^^^ ISSO CRIA UMA TABELA EXTRA SÓ PARA GUARDAR AS URLS ^^^^

    @Column(nullable = true, updatable = false)
    private LocalDateTime dataCriacao;

    private @NotNull(message = "A data de início é obrigatória.")
    @FutureOrPresent(message = "A data de início não pode ser no passado.") LocalDate dataInicio;

    private @NotNull(message = "A data de fim é obrigatória.") LocalDate dataFim;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusExperimento statusExperimento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pesquisador_id", nullable = true)
    private Pesquisador pesquisadorResponsavel;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String descricaoAmbiente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participante_id", nullable = true)
    private Participante participantePrincipal;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "experimento_participantes",
            joinColumns = @JoinColumn(name = "experimento_id"),
            inverseJoinColumns = @JoinColumn(name = "participante_id")
    )
    private Set<Participante> participantes = new HashSet<>();


    // Construtores
    public Experimento() {
        this.dataCriacao = LocalDateTime.now();
        this.statusExperimento = StatusExperimento.PLANEJADO;
    }

    public void addParticipante(Participante participante) {
        this.participantes.add(participante);
        participante.getExperimentos().add(this);
    }

    public void removeParticipante(Participante participante) {
        this.participantes.remove(participante);
        participante.getExperimentos().remove(this);
    }

    // VVVV GETTERS E SETTERS ATUALIZADOS VVVV
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricaoGeral() { return descricaoGeral; }
    public void setDescricaoGeral(String descricaoGeral) { this.descricaoGeral = descricaoGeral; }

    public String getResultadoEmocional() { return resultadoEmocional; }
    public void setResultadoEmocional(String resultadoEmocional) { this.resultadoEmocional = resultadoEmocional; }

    // Getter e Setter agora lidam com Lista
    public List<String> getUrlsMidia() { return urlsMidia; }
    public void setUrlsMidia(List<String> urlsMidia) { this.urlsMidia = urlsMidia; }

    // ^^^^ FIM DAS ATUALIZAÇÕES ^^^^

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
    public @NotNull(message = "A data de início é obrigatória.") @FutureOrPresent(message = "A data de início não pode ser no passado.") LocalDate getDataInicio() { return dataInicio; }
    public void setDataInicio(@NotNull(message = "A data de início é obrigatória.") @FutureOrPresent(message = "A data de início não pode ser no passado.") LocalDate dataInicio) { this.dataInicio = dataInicio; }
    public @NotNull(message = "A data de fim é obrigatória.") LocalDate getDataFim() { return dataFim; }
    public void setDataFim(@NotNull(message = "A data de fim é obrigatória.") LocalDate dataFim) { this.dataFim = dataFim; }
    public StatusExperimento getStatusExperimento() { return statusExperimento; }
    public void setStatusExperimento(StatusExperimento statusExperimento) { this.statusExperimento = statusExperimento; }
    public Pesquisador getPesquisadorResponsavel() { return pesquisadorResponsavel; }
    public void setPesquisadorResponsavel(Pesquisador pesquisadorResponsavel) { this.pesquisadorResponsavel = pesquisadorResponsavel; }
    public String getDescricaoAmbiente() { return descricaoAmbiente; }
    public void setDescricaoAmbiente(String descricaoAmbiente) { this.descricaoAmbiente = descricaoAmbiente; }
    public Set<Participante> getParticipantes() { return participantes; }
    public void setParticipantes(Set<Participante> participantes) { this.participantes = participantes; }

    public Participante getParticipantePrincipal() { return participantePrincipal; }
    public void setParticipantePrincipal(Participante participantePrincipal) { this.participantePrincipal = participantePrincipal; }
}