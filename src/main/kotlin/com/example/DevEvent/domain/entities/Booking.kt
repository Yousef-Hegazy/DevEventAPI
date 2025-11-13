package com.example.DevEvent.domain.entities

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.util.*

@Entity
@Table(
    name = "bookings",
    indexes = [
        Index(name = "idx_event_id", columnList = "event_id"),
        Index(name = "idx_event_created", columnList = "event_id, created_at"),
        Index(name = "idx_email", columnList = "email")
    ],
    uniqueConstraints = [
        UniqueConstraint(name = "uniq_event_email", columnNames = ["event_id", "email"])
    ]
)
@EntityListeners(AuditingEntityListener::class)
data class Booking(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id", nullable = false, foreignKey = ForeignKey(name = "fk_booking_event"))
    val event: Event,

    @Column(nullable = false, length = 255)
    var email: String,

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: Date? = null,

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    val updatedAt: Date? = null
) {
    @PrePersist
    @PreUpdate
    fun normalizeEmail() {
        email = email.trim().lowercase(Locale.getDefault())
    }
}

