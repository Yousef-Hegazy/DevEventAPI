package com.example.DevEvent.controllers

import com.example.DevEvent.domain.entities.Event
import com.example.DevEvent.domain.entities.EventMode
import com.example.DevEvent.dtos.EventDto
import com.example.DevEvent.mappers.toDto
import com.example.DevEvent.mappers.toDtoList
import com.example.DevEvent.mappers.toEntity
import com.example.DevEvent.services.EventService
import com.example.DevEvent.services.ImageService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.*
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/events")
@Tag(name = "Events", description = "Event management APIs")
@Validated
class EventController(
    private val eventService: EventService,
    private val imageService: ImageService
) {

    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @Operation(
        summary = "Create a new event",
        description = "Creates a new event with the provided details and uploads the event image"
    )
    fun createEvent(
        @NotNull
        @RequestParam("imageFile") imageFile: MultipartFile,

        @RequestParam("title")
        @NotBlank(message = "Title is required")
        @Size(max = 100, message = "Title cannot exceed 100 characters")
        title: String,

        @RequestParam("description")
        @NotBlank(message = "Description is required")
        @Size(max = 1000, message = "Description cannot exceed 1000 characters")
        description: String,

        @RequestParam("overview")
        @NotBlank(message = "Overview is required")
        @Size(max = 500, message = "Overview cannot exceed 500 characters")
        overview: String,

        @RequestParam("venue")
        @NotBlank(message = "Venue is required")
        venue: String,

        @RequestParam("location")
        @NotBlank(message = "Location is required")
        location: String,

        @RequestParam("date")
        @NotBlank(message = "Date is required")
        @Pattern(
            regexp = "^\\d{4}-\\d{2}-\\d{2}$",
            message = "Date must be in YYYY-MM-DD format"
        )
        date: String,

        @RequestParam("time")
        @NotBlank(message = "Time is required")
        @Pattern(
            regexp = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9](\\s*(AM|PM))?$",
            message = "Time must be in HH:MM or HH:MM AM/PM format"
        )
        time: String,

        @RequestParam("mode")
        @NotNull(message = "Mode is required")
        mode: EventMode,

        @RequestParam("audience")
        @NotBlank(message = "Audience is required")
        audience: String,

        @RequestParam("agenda")
        @NotEmpty(message = "Agenda is required")
        @Size(min = 1, message = "At least one agenda item is required")
        agenda: List<String>,

        @RequestParam("organizer")
        @NotBlank(message = "Organizer is required")
        organizer: String,

        @RequestParam("tags")
        @NotEmpty(message = "Tags are required")
        @Size(min = 1, max = 10, message = "At least one tag is required, maximum 10 tags allowed")
        tags: List<String>
    ): ResponseEntity<EventDto> {
        // Upload image to Cloudinary
        val imageUrl = imageService.uploadImage(imageFile)

        // Create the event entity directly
        val event = Event(
            id = null,
            title = title,
            description = description,
            overview = overview,
            image = imageUrl,
            venue = venue,
            location = location,
            date = date,
            time = time,
            mode = mode,
            audience = audience,
            agenda = agenda,
            organizer = organizer,
            tags = tags
        )

        return eventService.createEvent(event)
            .toDto()
            .let { ResponseEntity.status(HttpStatus.CREATED).body(it) }
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Get event by ID",
        description = "Retrieves an event by its unique identifier"
    )
    fun getEventById(
        @Parameter(description = "Event ID", required = true)
        @PathVariable id: Long
    ): ResponseEntity<EventDto> =
        eventService.getEventById(id)
            .toDto()
            .let { ResponseEntity.ok(it) }

    @GetMapping("/slug/{slug}")
    @Operation(
        summary = "Get event by slug",
        description = "Retrieves an event by its URL-friendly slug"
    )
    fun getEventBySlug(
        @Parameter(description = "Event slug", required = true, example = "kotlin-conference-2024")
        @PathVariable slug: String
    ): ResponseEntity<EventDto> =
        eventService.getEventBySlug(slug)
            .toDto()
            .let { ResponseEntity.ok(it) }

    @GetMapping
    @Operation(
        summary = "Get all events",
        description = "Retrieves a list of all events"
    )
    fun getAllEvents(): ResponseEntity<List<EventDto>> =
        ResponseEntity.ok(eventService.getAllEvents().toDtoList())

    @PutMapping("/{id}")
    @Operation(
        summary = "Update an event",
        description = "Updates an existing event with the provided details"
    )
    fun updateEvent(
        @Parameter(description = "Event ID", required = true)
        @PathVariable id: Long,
        @Valid @RequestBody dto: EventDto
    ): ResponseEntity<EventDto> =
        dto.toEntity()
            .let { eventService.updateEvent(id, it) }
            .toDto()
            .let { ResponseEntity.ok(it) }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Delete an event",
        description = "Deletes an event by its unique identifier"
    )
    fun deleteEvent(
        @Parameter(description = "Event ID", required = true)
        @PathVariable id: Long
    ): ResponseEntity<Void> =
        eventService.deleteEvent(id).let {
            ResponseEntity.noContent().build()
        }
}

