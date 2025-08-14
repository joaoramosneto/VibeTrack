package com.vibetrack.backend.users.Mapper;

import com.vibetrack.backend.users.DTO.participanteDTO.ParticipanteRequestDTO;
import com.vibetrack.backend.users.DTO.participanteDTO.ParticipanteResponseDTO;
import com.vibetrack.backend.users.Entity.Participante;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ParticipanteMapper {

    // Converte o DTO de Requisição para a Entidade
    @Mapping(target = "id", ignore = true) // Ignora o ID, pois será gerado pelo banco
    @Mapping(target = "experimentos", ignore = true) // Ignoramos a lista de experimentos na criação de um participante
    Participante toEntity(ParticipanteRequestDTO requestDTO);

    // Converte a Entidade para o DTO de Resposta
    ParticipanteResponseDTO toResponseDTO(Participante participante);

    // Converte uma lista de Entidades para uma lista de DTOs
    List<ParticipanteResponseDTO> toResponseDTOList(List<Participante> participantes);
}