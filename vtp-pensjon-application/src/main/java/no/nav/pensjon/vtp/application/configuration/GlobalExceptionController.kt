package no.nav.pensjon.vtp.application.configuration

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestController


@ControllerAdvice
@RestController
class GlobalExceptionController {
    data class JsonResponse(val message: String)

    @ExceptionHandler(value = [Exception::class])
    fun handleException(e: Exception): ResponseEntity<JsonResponse> {
        return ResponseEntity(JsonResponse(e.message?: ""), HttpStatus.BAD_REQUEST)
    }
}