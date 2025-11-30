package com.vibetrack.backend.users.Mapper;

import com.vibetrack.backend.users.Entity.Midia; // Importa a Entidade
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MidiaMapper {

    /**
     * Converte a Entidade Midia em uma URL de download para o Frontend.
     */
    default String toUrl(Midia midia) {
        if (midia == null || midia.getId() == null) {
            return null;
        }
        // O Frontend vai chamar este endpoint para baixar os bytes
        return "http://localhost:8080/api/midias/" + midia.getId();
    }
}
