package com.vibetrack.backend.users.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "midias")
public class Midia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String tipo; // Ex: image/png, image/jpeg

    // @Lob avisa ao banco que isso é um arquivo grande (BLOB)
    @Lob
    // columnDefinition ajuda a garantir compatibilidade entre H2 e Postgres
    @Column(columnDefinition = "BINARY LARGE OBJECT")
    private byte[] dados;

    // Construtor vazio (Obrigatório para o Hibernate)
    public Midia() {}

    // Construtor para facilitar a criação
    public Midia(String nome, String tipo, byte[] dados) {
        this.nome = nome;
        this.tipo = tipo;
        this.dados = dados;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public byte[] getDados() { return dados; }
    public void setDados(byte[] dados) { this.dados = dados; }
}