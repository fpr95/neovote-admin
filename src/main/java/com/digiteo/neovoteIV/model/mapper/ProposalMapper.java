package com.digiteo.neovoteIV.model.mapper;

import com.digiteo.neovoteIV.model.jpa.data.Proposal;
import com.digiteo.neovoteIV.web.data.model.ProposalUpdateData;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(
        componentModel = "spring"
)
public interface ProposalMapper {

    ProposalMapper INSTANCE = Mappers.getMapper(ProposalMapper.class);

    //@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "pud.name", target = "name", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "pud.contactEmail", target = "contactEmail", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "pud.details", target = "details", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "pud.visible", target = "visible", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patchToEntity(ProposalUpdateData pud, @MappingTarget Proposal p);
}
