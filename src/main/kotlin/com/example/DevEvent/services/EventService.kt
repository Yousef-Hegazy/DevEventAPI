package com.example.DevEvent.services

import com.example.DevEvent.domain.entities.Event

interface EventService {
    fun createEvent(event: Event): Event
    fun getEventById(id: Long): Event
    fun getEventBySlug(slug: String): Event
    fun getAllEvents(): List<Event>
    fun updateEvent(id: Long, updatedEvent: Event): Event
    fun deleteEvent(id: Long)
}

