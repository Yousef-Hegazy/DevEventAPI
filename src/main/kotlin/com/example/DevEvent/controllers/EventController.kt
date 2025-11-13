package com.example.DevEvent.controllers

import com.example.DevEvent.dtos.CreateEventRequest
import com.example.DevEvent.dtos.EventDto
import com.example.DevEvent.mappers.EventMapper
import com.example.DevEvent.services.EventService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/events")
class EventController(
    private val eventService: EventService,
    private val eventMapper: EventMapper
) {

    @PostMapping
    fun createEvent(@Valid @RequestBody request: CreateEventRequest): ResponseEntity<EventDto> =
        request.let(eventMapper::toEntity)
            .let(eventService::createEvent)
            .let(eventMapper::toDto)
            .let {
                ResponseEntity.status(HttpStatus.CREATED).body(it)
            }

    @GetMapping("/{id}")
    fun getEventById(@PathVariable id: Long): ResponseEntity<EventDto> =
        eventService.getEventById(id)
            .let(eventMapper::toDto)
            .let { ResponseEntity.ok(it) }

    @GetMapping("/slug/{slug}")
    fun getEventBySlug(@PathVariable slug: String): ResponseEntity<EventDto> =
        eventService.getEventBySlug(slug)
            .let(eventMapper::toDto)
            .let { ResponseEntity.ok(it) }

    @GetMapping
    fun getAllEvents(): ResponseEntity<List<EventDto>> {
        val events = eventService.getAllEvents()
        return ResponseEntity.ok(eventMapper.toDtoList(events))
    }

    @PutMapping("/{id}")
    fun updateEvent(
        @PathVariable id: Long,
        @Valid @RequestBody dto: EventDto
    ): ResponseEntity<EventDto> =
        dto.let(eventMapper::toEntity)
            .let { eventService.updateEvent(id, it) }
            .let(eventMapper::toDto)
            .let { ResponseEntity.ok(it) }

    @DeleteMapping("/{id}")
    fun deleteEvent(@PathVariable id: Long): ResponseEntity<Void> =
        eventService.deleteEvent(id).let {
            ResponseEntity.noContent().build()
        }
}

