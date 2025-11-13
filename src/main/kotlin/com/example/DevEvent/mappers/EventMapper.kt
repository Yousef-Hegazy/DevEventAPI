package com.example.DevEvent.mappers

import com.example.DevEvent.domain.entities.Event
import com.example.DevEvent.dtos.CreateEventRequest
import com.example.DevEvent.dtos.EventDto
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingConstants

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
interface EventMapper {

    // Entity to DTO
    fun toDto(event: Event): EventDto

    // DTO to Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "slug", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    fun toEntity(dto: EventDto): Event

    // CreateEventRequest to Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "slug", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    fun toEntity(request: CreateEventRequest): Event

    // List mappings
    fun toDtoList(events: List<Event>): List<EventDto>
}

