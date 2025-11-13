package com.example.DevEvent.dtos

import com.example.DevEvent.domain.entities.Event
import com.example.DevEvent.domain.entities.EventMode
import jakarta.validation.constraints.*
import java.util.*

data class EventDto(
    val id: Long? = null,

    @field:NotBlank(message = "Title is required")
    @field:Size(max = 100, message = "Title cannot exceed 100 characters")
    val title: String,

    @field:NotBlank(message = "Description is required")
    @field:Size(max = 1000, message = "Description cannot exceed 1000 characters")
    val description: String,

    @field:NotBlank(message = "Overview is required")
    @field:Size(max = 500, message = "Overview cannot exceed 500 characters")
    val overview: String,

    @field:NotBlank(message = "Image URL is required")
    @field:Pattern(
        regexp = "^(https?://|//).*\\.(jpg|jpeg|png|gif|webp|svg)$",
        message = "Please provide a valid image URL",
        flags = [Pattern.Flag.CASE_INSENSITIVE]
    )
    val image: String,

    @field:NotBlank(message = "Venue is required")
    val venue: String,

    @field:NotBlank(message = "Location is required")
    val location: String,

    @field:NotBlank(message = "Date is required")
    @field:Pattern(
        regexp = "^\\d{4}-\\d{2}-\\d{2}$",
        message = "Date must be in YYYY-MM-DD format"
    )
    val date: String,

    @field:NotBlank(message = "Time is required")
    @field:Pattern(
        regexp = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9](\\s*(AM|PM))?$",
        message = "Time must be in HH:MM or HH:MM AM/PM format",
        flags = [Pattern.Flag.CASE_INSENSITIVE]
    )
    val time: String,

    @field:NotNull(message = "Mode is required")
    var mode: EventMode,

    @field:NotBlank(message = "Audience is required")
    val audience: String,

    @field:NotEmpty(message = "Agenda is required")
    @field:Size(min = 1, message = "At least one agenda item is required")
    val agenda: List<@NotBlank(message = "Agenda item cannot be blank") String>,

    @field:NotBlank(message = "Organizer is required")
    val organizer: String,

    @field:NotEmpty(message = "Tags are required")
    @field:Size(min = 1, max = 10, message = "At least one tag is required, maximum 10 tags allowed")
    val tags: List<@NotBlank(message = "Tag cannot be blank") String>,

    val slug: String? = null,

    val createdAt: Date? = null,

    val updatedAt: Date? = null
)
