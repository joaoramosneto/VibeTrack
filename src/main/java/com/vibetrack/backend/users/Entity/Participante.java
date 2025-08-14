package com.vibetrack.backend.users.Entity;

import jakarta.persistence.*;
// import javax.persistence.*; // Para Spring Boot 2.x
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "participantes")
public class Participante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nomeCompleto;

    @Column(unique = true) // Se o email for usado como identificador único
    private String email;

    private LocalDate dataNascimento;

    // @Lob
    // @Column(columnDefinition = "TEXT")
    // private String outrosDadosDemograficos; // Exemplo para armazenar como JSON

    @ManyToMany(mappedBy = "participantes", fetch = FetchType.LAZY)
    private Set<Experimento> experimentos = new HashSet<>();

    // Construtores
    public Participante() {
    }

    // Getters e Setters (omitidos por brevidade, mas são necessários)
    // Lembre-se de usar Lombok @Getter @Setter @NoArgsConstructor @AllArgsConstructor se preferir

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNomeCompleto() { return nomeCompleto; }
    public void setNomeCompleto(String nomeCompleto) { this.nomeCompleto = nomeCompleto; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }
    public Set<Experimento> getExperimentos() { return experimentos; }
    public void setExperimentos(Set<Experimento> experimentos) { this.experimentos = experimentos; }
}
