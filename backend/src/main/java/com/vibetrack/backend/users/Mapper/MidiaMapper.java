package com.vibetrack.backend.users.Mapper;

import com.vibetrack.backend.users.DTO.MidiaResponseDTO; // VVVV CORREÇÃO: Import direto do DTO VVVV
import com.vibetrack.backend.users.Entity.Midia;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface MidiaMapper {

    // Converte Entidade -> DTO Completo
    @Mapping(target = "url", source = ".", qualifiedByName = "gerarUrl")
    MidiaResponseDTO toDTO(Midia midia);

    // Método auxiliar para criar a URL
    @Named("gerarUrl")
    default String gerarUrl(Midia midia) {
        if (midia == null || midia.getId() == null) {
            return null;
        }
        return "http://localhost:8080/api/midias/" + midia.getId();
    }
}