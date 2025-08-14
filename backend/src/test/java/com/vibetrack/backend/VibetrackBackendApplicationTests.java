package com.vibetrack.backend; // Certifique-se de que o pacote está correto!

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
@SpringBootTest // Esta anotação é crucial, ela carrega o contexto completo do Spring Boot para o teste
class VibetrackBackendApplicationTests {

    @Test // Indica que este é um método de teste
    void contextLoads() {
        // Este teste é muito simples: ele apenas verifica se o contexto da aplicação
        // Spring consegue ser carregado com sucesso. Se não houver erros ao carregar
        // os beans e as configurações, o teste passa.
    }

}