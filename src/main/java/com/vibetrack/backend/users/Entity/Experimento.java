package com.vibetrack.backend.users.Entity;

import com.vibetrack.backend.users.Entity.Enums.StatusExperimento;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
// import javax.persistence.*; // Para Spring Boot 2.x
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "experimentos")
public class Experimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Lob // Para textos mais longos
    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(nullable = true, updatable = false) // Não atualizável após criação
    private LocalDateTime dataCriacao;

    private @NotNull(message = "A data de início é obrigatória.")
    @FutureOrPresent(message = "A data de início não pode ser no passado.") LocalDate dataInicio;

    private @NotNull(message = "A data de fim é obrigatória.") LocalDate dataFim;

    @Enumerated(EnumType.STRING) // Grava o nome do Enum no banco
    @Column(nullable = false)
    private StatusExperimento statusExperimento;

    @ManyToOne(fetch = FetchType.LAZY) // LAZY é geralmente melhor para performance
    @JoinColumn(name = "pesquisador_id", nullable = true)
    private Pesquisador pesquisadorResponsavel;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String descricaoAmbiente;

    // Relacionamento com Participantes (um experimento pode ter muitos participantes)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "experimento_participantes",
            joinColumns = @JoinColumn(name = "experimento_id"),
            inverseJoinColumns = @JoinColumn(name = "participante_id")
    )
    private Set<Participante> participantes = new HashSet<>();

    // Se você criar entidades separadas para Video, Audio, DadoSmartwatch:
    // @OneToMany(mappedBy = "experimento", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    // private List<Video> videos = new ArrayList<>();

    // @OneToMany(mappedBy = "experimento", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    // private List<Audio> audios = new ArrayList<>();

    // @OneToMany(mappedBy = "experimento", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    // private List<DadoSmartwatch> dadosSmartwatch = new ArrayList<>();


    // Construtores
    public Experimento() {
        this.dataCriacao = LocalDateTime.now(); // Define a data de criação automaticamente
        this.statusExperimento = StatusExperimento.PLANEJADO; // Status inicial padrão
    }

    // Getters e Setters (omitidos por brevidade, mas são necessários)
    // Lembre-se de usar Lombok @Getter @Setter @NoArgsConstructor @AllArgsConstructor se preferir

    // Métodos utilitários para gerenciar relacionamentos bidirecionais (se aplicável)
    // public void addVideo(Video video) {
    //     videos.add(video);
    //     video.setExperimento(this);
    // }
    // public void removeVideo(Video video) {
    //     videos.remove(video);
    //     video.setExperimento(null);
    // }
    // ... (similar para Audio e DadoSmartwatch)

    public void addParticipante(Participante participante) {
        this.participantes.add(participante);
        participante.getExperimentos().add(this); // Mantém o lado inverso sincronizado
    }

    public void removeParticipante(Participante participante) {
        this.participantes.remove(participante);
        participante.getExperimentos().remove(this);
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
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
}
