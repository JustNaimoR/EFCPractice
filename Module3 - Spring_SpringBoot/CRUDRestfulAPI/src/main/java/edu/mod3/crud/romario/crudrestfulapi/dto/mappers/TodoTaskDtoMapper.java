package edu.mod3.crud.romario.crudrestfulapi.dto.mappers;

import edu.mod3.crud.romario.crudrestfulapi.dto.TodoTaskDto;
import edu.mod3.crud.romario.crudrestfulapi.entities.TodoTask;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TodoTaskDtoMapper {
    @Mappings({
            @Mapping(target = "title", source = "title"),
            @Mapping(target = "description", source = "description")
    })
    TodoTaskDto toDto(TodoTask task);
    @Mappings({
            @Mapping(target = "title", source = "title"),
            @Mapping(target = "description", source = "description")
    })
    TodoTask fromDto(TodoTaskDto dto);
}