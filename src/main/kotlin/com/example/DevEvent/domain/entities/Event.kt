package com.example.DevEvent.domain.entities

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

@Entity
@Table(
    name = "events",
    indexes = [
        Index(name = "idx_slug", columnList = "slug", unique = true),
        Index(name = "idx_date_mode", columnList = "date, mode")
    ]
)
@EntityListeners(AuditingEntityListener::class)
data class Event(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long?,

    @Column(length = 100, unique = true, nullable = false)
    val title: String,

    @Column(unique = true, nullable = false)
    var slug: String? = null,

    @Column(nullable = false, length = 1000)
    val description: String,

    @Column(nullable = false, length = 500)
    val overview: String,

    @Column(nullable = false)
    val image: String,

    @Column(nullable = false)
    val venue: String,

    @Column(nullable = false)
    val location: String,

    @Column(nullable = false)
    var date: String,

    @Column(nullable = false)
    var time: String,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val mode: EventMode,

    @Column(nullable = false)
    val audience: String,

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "event_agenda", joinColumns = [JoinColumn(name = "event_id")])
    @Column(name = "agenda_item", nullable = false)
    val agenda: List<String> = emptyList(),

    @Column(nullable = false)
    val organizer: String,

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "event_tags", joinColumns = [JoinColumn(name = "event_id")])
    @Column(name = "tag", nullable = false)
    val tags: List<String> = emptyList(),

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: Date? = null,

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    val updatedAt: Date? = null
) {

    @PrePersist
    @PreUpdate
    fun normalizeData() {
        // Generate slug only if it's null or blank
        if (slug.isNullOrBlank()) {
            slug = generateSlug(title)
        }

        // Normalize date to YYYY-MM-DD format
        date = normalizeDate(date)

        // Normalize time to HH:MM format
        time = normalizeTime(time)
    }

    private fun generateSlug(title: String): String {
        return title
            .lowercase(Locale.getDefault())
            .trim()
            .replace(Regex("[^a-z0-9\\s-]"), "") // Remove special characters
            .replace(Regex("\\s+"), "-") // Replace spaces with hyphens
            .replace(Regex("-+"), "-") // Replace multiple hyphens with single hyphen
            .replace(Regex("^-|-$"), "") // Remove leading/trailing hyphens
    }

    private fun normalizeDate(dateString: String): String {
        return try {
            val localDate = LocalDate.parse(dateString, DateTimeFormatter.ISO_DATE)
            localDate.toString() // Returns YYYY-MM-DD format
        } catch (e: Exception) {
            // If already in correct format or parsing fails, return as is
            dateString
        }
    }

    private fun normalizeTime(timeString: String): String {
        return try {
            // Handle various time formats and convert to HH:MM (24-hour format)
            val timeRegex = Regex("""^(\d{1,2}):(\d{2})(\s*(AM|PM))?$""", RegexOption.IGNORE_CASE)
            val match = timeRegex.find(timeString.trim())

            if (match != null) {
                var hours = match.groupValues[1].toInt()
                val minutes = match.groupValues[2]
                val period = match.groupValues[4].uppercase()

                if (period.isNotEmpty()) {
                    // Convert 12-hour to 24-hour format
                    if (period == "PM" && hours != 12) hours += 12
                    if (period == "AM" && hours == 12) hours = 0
                }

                if (hours in 0..23 && minutes.toInt() in 0..59) {
                    return "${hours.toString().padStart(2, '0')}:$minutes"
                }
            }

            // Try parsing as LocalTime
            val localTime = LocalTime.parse(timeString, DateTimeFormatter.ISO_LOCAL_TIME)
            localTime.format(DateTimeFormatter.ofPattern("HH:mm"))
        } catch (e: Exception) {
            // If already in correct format or parsing fails, return as is
            timeString
        }
    }
}
