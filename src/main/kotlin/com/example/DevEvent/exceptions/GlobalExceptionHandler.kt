package com.example.DevEvent.exceptions

import com.example.DevEvent.domain.entities.EventMode
import jakarta.persistence.EntityNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    private val validEventModes = EventMode.entries.joinToString(", ") { it.name }

    @ExceptionHandler(Exception::class)
    fun handleException(ex: Exception) = ProblemDetail
        .forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.message)

    @ExceptionHandler(EntityNotFoundException::class)
    fun handleException(ex: EntityNotFoundException) = ProblemDetail
        .forStatusAndDetail(HttpStatus.NOT_FOUND, ex.message)

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleException(ex: HttpMessageNotReadableException): ProblemDetail {
        val message = ex.message ?: "Invalid request body"

        // Extract more readable error message
        val readableMessage = when {
            message.contains("Cannot coerce empty String") && message.contains("EventMode") -> {
                "Event mode is required and cannot be empty. Valid values are: $validEventModes"
            }

            message.contains("not one of the values accepted for Enum class") && message.contains("EventMode") -> {
                "Invalid event mode. Valid values are: $validEventModes"
            }

            message.contains("EventMode") -> {
                "Invalid event mode. Valid values are: $validEventModes"
            }

            message.contains("Cannot deserialize value of type") -> {
                "Invalid value provided for one or more fields. Please check your request body format."
            }

            message.contains("Required request body is missing") -> {
                "Request body is required"
            }

            message.contains("JSON parse error") -> {
                "Invalid JSON format. Please check your request body."
            }

            else -> "Invalid request body format"
        }

        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, readableMessage).also {
            // Include the technical details for debugging (optional)
            it.setProperty("technicalDetails", message)
        }
    }


    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleException(ex: MethodArgumentNotValidException) = ProblemDetail
        .forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation failed").also {
            it.setProperty(
                "errors",
                ex.bindingResult.fieldErrors.associate { field ->
                    field.field to (field.defaultMessage ?: "Invalid Value")
                }
            )
        }

//    @ExceptionHandler(MissingServletRequestPartException::class)
//    fun handleException(ex: MissingServletRequestPartException) = ProblemDetail
//        .forStatusAndDetail(
//            HttpStatus.BAD_REQUEST,
//            "Missing required part: ${ex.requestPartName}. Please ensure you're sending both 'imageFile' (file) and 'event' (JSON) as separate parts in multipart/form-data."
//        )

//    @ExceptionHandler(MissingServletRequestPartException::class)
//    fun handleMissingPartException(ex: MissingServletRequestPartException) = ProblemDetail
//        .forStatusAndDetail(
//            HttpStatus.BAD_REQUEST,
//            "Missing required part: ${ex.requestPartName}. Expected parts: 'imageFile' (file) and 'event' (JSON)"
//        )

}