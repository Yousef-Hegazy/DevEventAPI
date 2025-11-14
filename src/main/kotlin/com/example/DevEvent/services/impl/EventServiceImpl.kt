package com.example.DevEvent.services.impl

import com.example.DevEvent.domain.entities.Event
import com.example.DevEvent.repositories.EventRepository
import com.example.DevEvent.services.EventService
import jakarta.persistence.EntityNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class EventServiceImpl(
    private val eventRepository: EventRepository
) : EventService {

    @Transactional
    override fun createEvent(event: Event): Event =
        event.also {
            require(it.id == null) { "Event id must not be provided" }
            require(!eventRepository.existsByTitle(it.title)) { "This event already exists" }
        }
            .let(eventRepository::save)

    override fun getEventById(id: Long): Event =
        eventRepository.findByIdOrNull(id) ?: throw EntityNotFoundException("Event with id: $id not found")

    override fun getEventBySlug(slug: String): Event =
        eventRepository.findBySlug(slug)
            .orElseThrow { EntityNotFoundException("Event with slug: $slug does not exist") }

    override fun getAllEvents(): List<Event> =
        eventRepository.findAll()

    override fun updateEvent(id: Long, updatedEvent: Event): Event =
        eventRepository.findById(id)
            .map {
                it.copy(
                    title = updatedEvent.title,
                    description = updatedEvent.description,
                    overview = updatedEvent.overview,
                    image = updatedEvent.image,
                    venue = updatedEvent.venue,
                    location = updatedEvent.location,
                    date = updatedEvent.date,
                    time = updatedEvent.time,
                    mode = updatedEvent.mode,
                    audience = updatedEvent.audience,
                    agenda = updatedEvent.agenda,
                    organizer = updatedEvent.organizer,
                    tags = updatedEvent.tags
                )
            }
            .map(eventRepository::save)
            .orElseThrow { EntityNotFoundException("Event with id: $id not found") }

    override fun deleteEvent(id: Long) {
        eventRepository.findById(id)
            .map(eventRepository::delete)
            .orElseThrow { EntityNotFoundException("Event with id: $id not found") }
    }
}

