package com.vibetrack.backend.users.Entity;

import jakarta.persistence.*; // Para JPA Jakarta EE (Spring Boot 3+)
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
// import javax.persistence.*; // Para JPA legada (Spring Boot 2.x)
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "pesquisadores") // Define o nome da tabela no banco de dados
public class Pesquisador implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Autoincremento pelo banco
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true) // Email deve ser único
    private String email;

    @Column(nullable = false)
    private String senha; // Lembre-se de criptografar antes de salvar!

    // Exemplo de relacionamento (se você tiver uma entidade Papel/Role)
    // @ManyToMany(fetch = FetchType.EAGER)
    // @JoinTable(
    // name = "pesquisador_papeis",
    // joinColumns = @JoinColumn(name = "pesquisador_id"),
    // inverseJoinColumns = @JoinColumn(name = "papel_id")
    // )
    // private Set<Papel> papeis;

    // Construtores
    public Pesquisador() {
    }

    public Pesquisador(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 2. Define os "papéis" ou "permissões" do usuário.
        // Por enquanto, todo pesquisador terá o papel "ROLE_USER".
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        // 3. Retorna a senha (já criptografada)
        return this.senha;
    }

    @Override
    public String getUsername() {
        // 4. Retorna o campo que será usado como "username" para o login.
        // No nosso caso, é o email.
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        // 5. Indica se a conta não expirou.
        return true; // Deixamos true por padrão
    }

    @Override
    public boolean isAccountNonLocked() {
        // 6. Indica se a conta não está bloqueada.
        return true; // Deixamos true por padrão
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // 7. Indica se as credenciais (senha) não expiraram.
        return true; // Deixamos true por padrão
    }

    @Override
    public boolean isEnabled() {
        // 8. Indica se a conta está ativa.
        // (Futuramente, podemos usar isso para a confirmação de email)
        return true; // Deixamos true por padrão
    }

    // Getters e Setters para 'papeis' se você adicionar
    // public Set<Papel> getPapeis() {
    //     return papeis;
    // }

    // public void setPapeis(Set<Papel> papeis) {
    //     this.papeis = papeis;
    // }

    // É uma boa prática adicionar equals() e hashCode() se você for trabalhar
    // com estas entidades em coleções ou se elas forem desanexadas e reanexadas
    // pelo EntityManager. Para simplificar, omiti por agora.
}
