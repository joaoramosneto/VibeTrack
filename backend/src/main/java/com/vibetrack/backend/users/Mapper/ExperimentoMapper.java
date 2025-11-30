package com.vibetrack.backend.users.Mapper;

import com.vibetrack.backend.users.DTO.ExperimentoRequestDTO;
import com.vibetrack.backend.users.DTO.ExperimentoResponseDTO;
import com.vibetrack.backend.users.Entity.Experimento;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

// VVVV IMPORTANTE: Adicionamos MidiaMapper aqui VVVV
@Mapper(componentModel = "spring", uses = {PesquisadorMapper.class, ParticipanteMapper.class, MidiaMapper.class})
public interface ExperimentoMapper {

    // --- DE DTO PARA ENTIDADE ---
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "participantes", ignore = true)
    @Mapping(target = "pesquisadorResponsavel", ignore = true)
    @Mapping(target = "participantePrincipal", ignore = true) // Service busca isso

    // Mapeia nomes diferentes
    @Mapping(source = "descricaoAmbiente", target = "descricaoGeral")
    @Mapping(source = "tipoEmocao", target = "resultadoEmocional")

    // Ignora a lista de midias no Request (o Service lida com MultipartFile separadamente)
    @Mapping(target = "midias", ignore = true)

    // Ignora campos que só existem no DTO
    @Mapping(target = "descricaoAmbiente", ignore = true)

    Experimento toEntity(ExperimentoRequestDTO dto);


    // --- DE ENTIDADE PARA DTO (RESPOSTA) ---
    @Mapping(source = "descricaoGeral", target = "descricaoGeral")
    @Mapping(source = "resultadoEmocional", target = "resultadoEmocional")

    // VVVV A MÁGICA DO BLOB VVVV
    // O MapStruct vai usar o MidiaMapper.toUrl() para converter cada Midia da lista em uma String URL
    @Mapping(source = "midias", target = "urlsMidia")
    // ^^^^ FIM ^^^^

    @Mapping(source = "participantePrincipal", target = "participantePrincipal")
    @Mapping(source = "pesquisadorResponsavel", target = "pesquisador")

    ExperimentoResponseDTO toResponseDTO(Experimento entity);

    List<ExperimentoResponseDTO> toResponseDTOList(List<Experimento> entities);
}