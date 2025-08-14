// no pacote Mapper
package com.vibetrack.backend.users.Mapper;

import com.vibetrack.backend.users.DTO.pesquisadorDTO.PesquisadorRequestDTO;
import com.vibetrack.backend.users.DTO.pesquisadorDTO.PesquisadorResponseDTO;
import com.vibetrack.backend.users.Entity.Pesquisador;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PesquisadorMapper {
    PesquisadorResponseDTO toResponseDTO(Pesquisador pesquisador);

    @Mapping(target = "id", ignore = true) // Ignora o ID, pois será gerado pelo banco
    Pesquisador toEntity(PesquisadorRequestDTO requestDTO);
}