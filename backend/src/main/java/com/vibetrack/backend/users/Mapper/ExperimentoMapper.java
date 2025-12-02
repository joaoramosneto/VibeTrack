package com.vibetrack.backend.users.Mapper;

import com.vibetrack.backend.users.DTO.ExperimentoRequestDTO;
import com.vibetrack.backend.users.DTO.ExperimentoResponseDTO;
import com.vibetrack.backend.users.Entity.Experimento;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring", uses = {PesquisadorMapper.class, ParticipanteMapper.class, MidiaMapper.class})
public interface ExperimentoMapper {

    // --- REQUEST -> ENTIDADE ---
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "participantes", ignore = true)
    @Mapping(target = "pesquisadorResponsavel", ignore = true)
    @Mapping(target = "participantePrincipal", ignore = true)
    @Mapping(source = "descricaoAmbiente", target = "descricaoGeral")
    @Mapping(source = "tipoEmocao", target = "resultadoEmocional")
    @Mapping(target = "midias", ignore = true)
    @Mapping(target = "descricaoAmbiente", ignore = true)
    Experimento toEntity(ExperimentoRequestDTO dto);

    // --- ENTIDADE -> RESPONSE ---
    @Mapping(source = "descricaoGeral", target = "descricaoGeral")
    @Mapping(source = "resultadoEmocional", target = "resultadoEmocional")
    @Mapping(source = "midias", target = "midias")
    @Mapping(source = "participantePrincipal", target = "participantePrincipal")
    @Mapping(source = "pesquisadorResponsavel", target = "pesquisador")

    // VVVV ADIÇÃO IMPORTANTE: Garantir que a lista de participantes seja mapeada VVVV
    @Mapping(source = "participantes", target = "participantes")
    // ^^^^ FIM ^^^^

    ExperimentoResponseDTO toResponseDTO(Experimento entity);

    List<ExperimentoResponseDTO> toResponseDTOList(List<Experimento> entities);
}