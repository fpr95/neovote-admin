package com.digiteo.neovoteIV.model.mapper;

import com.digiteo.neovoteIV.model.jpa.data.Proposal;
import com.digiteo.neovoteIV.web.data.model.ProposalData;
import com.digiteo.neovoteIV.web.data.model.ProposalUpdateData;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ProposalMapper {

    ProposalMapper INSTANCE = Mappers.getMapper(ProposalMapper.class);

    Proposal toEntity(ProposalData dto);
    ProposalData toDto(Proposal entity);

    @BeforeMapping
    default void setEmptySourceStringToNull(@MappingTarget Proposal entity, ProposalUpdateData dto){
        if (dto.getName().isEmpty()){
            dto.setName(null);
        }
        if (dto.getContactEmail().isEmpty()){
            dto.setContactEmail(null);
        }
        if (dto.getDetails().isEmpty()){
            dto.setDetails(null);
        }
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "name", target = "name", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "contactEmail", target = "contactEmail", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "details", target = "details", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "visible", target = "visible", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Proposal patchToEntity(@MappingTarget Proposal entity, ProposalUpdateData dto);
}
