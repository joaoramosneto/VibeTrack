package com.vibetrack.backend.users.Repository;

import com.vibetrack.backend.users.Entity.Pesquisador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional; // <-- NOVO IMPORT

@Repository
public interface PesquisadorRepository extends JpaRepository<Pesquisador, Long> {

    Pesquisador findByEmail(String email);

    // vvvv ADICIONE APENAS ESTA LINHA vvvv
    // Spring Data JPA vai entender, pelo nome, que este método deve
    // procurar um Pesquisador pela sua propriedade "codigoVerificacao".
    Optional<Pesquisador> findByCodigoVerificacao(String codigo);
    // ^^^^ FIM DA ADIÇÃO ^^^^
}