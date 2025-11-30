package com.vibetrack.backend.users.Mapper;

import com.vibetrack.backend.users.DTO.ExperimentoRequestDTO;
import com.vibetrack.backend.users.DTO.ExperimentoResponseDTO;
import com.vibetrack.backend.users.Entity.Experimento;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring", uses = {PesquisadorMapper.class, ParticipanteMapper.class})
public interface ExperimentoMapper {

    // VVVV REQUEST (JSON) -> ENTIDADE (BANCO) VVVV
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "participantes", ignore = true)
    @Mapping(target = "pesquisadorResponsavel", ignore = true)

    @Mapping(source = "descricaoAmbiente", target = "descricaoGeral")
    @Mapping(source = "tipoEmocao", target = "resultadoEmocional")

    // VVVV MUDANÇA: Agora mapeamos a lista urlsMidia VVVV
    @Mapping(source = "urlsMidia", target = "urlsMidia")
    // ^^^^ FIM DA MUDANÇA ^^^^

    @Mapping(target = "descricaoAmbiente", ignore = true)

    // Ignora o ID do participante no Request, pois o Service fará a busca da Entidade
    @Mapping(target = "participantePrincipal", ignore = true)

    Experimento toEntity(ExperimentoRequestDTO dto);


    // VVVV ENTIDADE (BANCO) -> RESPONSE (JSON) VVVV
    @Mapping(source = "descricaoGeral", target = "descricaoGeral")
    @Mapping(source = "resultadoEmocional", target = "resultadoEmocional")

    // VVVV MUDANÇA: Mapeamos a lista de volta para o DTO VVVV
    @Mapping(source = "urlsMidia", target = "urlsMidia")
    // ^^^^ FIM DA MUDANÇA ^^^^

    @Mapping(source = "participantePrincipal", target = "participantePrincipal")

    @Mapping(source = "pesquisadorResponsavel", target = "pesquisador")
    ExperimentoResponseDTO toResponseDTO(Experimento entity);

    List<ExperimentoResponseDTO> toResponseDTOList(List<Experimento> entities);
}