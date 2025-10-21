// Crie este novo arquivo: ExperimentoMapper.java
package com.vibetrack.backend.users.Mapper; // Sugestão de pacote para mappers

import com.vibetrack.backend.users.DTO.ExperimentoRequestDTO;
import com.vibetrack.backend.users.DTO.ExperimentoResponseDTO;
import com.vibetrack.backend.users.Entity.Experimento;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring", uses = {PesquisadorMapper.class, ParticipanteMapper.class}) // Diz ao MapStruct para gerar uma implementação que é um Spring Bean
public interface ExperimentoMapper {

    // Converte um DTO de request para uma Entidade
    @Mapping(target = "id", ignore = true) // Ignora o ID, pois será gerado pelo banco
    @Mapping(target = "dataCriacao", ignore = true) // Ignora, pois será gerenciado pela entidade ou banco
    @Mapping(target = "participantes", ignore = true) // Ignora, pois será gerenciado em outro momento
    @Mapping(target = "pesquisadorResponsavel", ignore = true) // Ignora, pois o Service irá preencher
    @Mapping(target = "descricaoAmbiente", ignore = true)

    Experimento toEntity(ExperimentoRequestDTO dto);

    // Converte uma Entidade para um DTO de response
    @Mapping(source = "pesquisadorResponsavel", target = "pesquisador")
    ExperimentoResponseDTO toResponseDTO(Experimento entity);

    // Converte uma lista de Entidades para uma lista de DTOs de response
    List<ExperimentoResponseDTO> toResponseDTOList(List<Experimento> entities);
}