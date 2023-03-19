package com.digiteo.neovoteIV.model.mapper;

import com.digiteo.neovoteIV.model.jpa.data.Election;
import com.digiteo.neovoteIV.web.data.model.ElectionData;
import com.digiteo.neovoteIV.web.data.model.ElectionUpdateData;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ElectionMapper {

    ElectionMapper INSTANCE = Mappers.getMapper(ElectionMapper.class);

    Election toEntity(ElectionData dto);
    ElectionData toDto(Election entity);

    @BeforeMapping
    default void setEmptySourceStringToNull(@MappingTarget Election entity, ElectionUpdateData dto){
        if (dto.getEditableTitle().isEmpty()) {
            dto.setEditableTitle(null);
        }
        if (dto.getBriefDescription().isEmpty()){
            dto.setBriefDescription(null);
        }
        if (dto.getEditableDetails().isEmpty()){
            dto.setEditableDetails(null);
        }
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "editableTitle", target = "title", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "topics", target = "topics", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "briefDescription", target = "briefDescription", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "initTimestamp", target = "initTimestamp", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "finishTimestamp", target = "finishTimestamp", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "editableDetails", target = "details", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Election patchToEntity(@MappingTarget Election entity, ElectionUpdateData dto);

}
