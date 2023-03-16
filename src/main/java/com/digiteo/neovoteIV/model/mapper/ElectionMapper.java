package com.digiteo.neovoteIV.model.mapper;

import com.digiteo.neovoteIV.model.jpa.data.Election;
import com.digiteo.neovoteIV.web.data.model.ElectionUpdateData;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mapper(
        componentModel = "spring"
)
public interface ElectionMapper {

    Logger logger = LoggerFactory.getLogger(ElectionMapper.class);
    ElectionMapper INSTANCE = Mappers.getMapper(ElectionMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "eud.editableTitle", target = "title", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "eud.topics", target = "topics", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "eud.briefDescription", target = "briefDescription", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "eud.initTimestamp", target = "initTimestamp", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "eud.finishTimestamp", target = "finishTimestamp", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "eud.details", target = "details", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Election patchToEntity(ElectionUpdateData eud,@MappingTarget Election e);


    default void logMappingResult(ElectionUpdateData eud, @MappingTarget Election e){
        logger.debug("Mapper method called with source={}, target={}", eud, e);
    }
}
