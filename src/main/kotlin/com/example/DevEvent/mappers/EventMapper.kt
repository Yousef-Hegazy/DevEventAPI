package com.example.DevEvent.mappers

import com.example.DevEvent.domain.entities.Event
import com.example.DevEvent.dtos.CreateEventRequest
import com.example.DevEvent.dtos.EventDto

// Extension function: Entity to DTO
fun Event.toDto(): EventDto = EventDto(
    id = id,
    title = title,
    description = description,
    overview = overview,
    image = image,
    venue = venue,
    location = location,
    date = date,
    time = time,
    mode = mode,
    audience = audience,
    agenda = agenda,
    organizer = organizer,
    tags = tags,
    slug = slug,
    createdAt = createdAt,
    updatedAt = updatedAt
)

// Extension function: DTO to Entity
fun EventDto.toEntity(): Event = Event(
    id = id,
    title = title,
    description = description,
    overview = overview,
    image = image,
    venue = venue,
    location = location,
    date = date,
    time = time,
    mode = mode,
    audience = audience,
    agenda = agenda,
    organizer = organizer,
    tags = tags,
    slug = slug,
    createdAt = createdAt,
    updatedAt = updatedAt
)

// Extension function: CreateEventRequest to Entity
fun CreateEventRequest.toEntity(): Event = Event(
    id = null,
    title = title,
    description = description,
    overview = overview,
    image = image,
    venue = venue,
    location = location,
    date = date,
    time = time,
    mode = mode,
    audience = audience,
    agenda = agenda,
    organizer = organizer,
    tags = tags,
    slug = null,
    createdAt = null,
    updatedAt = null
)

// Extension function: List<Event> to List<EventDto>
fun List<Event>.toDtoList(): List<EventDto> = map { it.toDto() }

