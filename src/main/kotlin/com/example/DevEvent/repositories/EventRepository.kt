package com.example.DevEvent.repositories

import com.example.DevEvent.domain.entities.Event
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface EventRepository : JpaRepository<Event, Long> {
    fun findBySlug(slug: String): Optional<Event>
    fun existsBySlug(slug: String): Boolean
    fun existsByTitle(title: String): Boolean
}

