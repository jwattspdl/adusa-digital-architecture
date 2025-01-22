package com.peapod.example.resilience.app.http

import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus


@ControllerAdvice("com.peapod.example.resilience.app.http")
class ProductSearchExceptionHandler {

    @ExceptionHandler(Exception::class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    fun generalSearchException(req: HttpServletRequest, exception: Exception): ResponseEntity<Map<String, Any>> {
        logger.error("Unexpected error in Product API. Returning 500 error to calling client.", exception)
        val body = mutableMapOf<String, Any>()
        body.put("code", "UNEXPECTED_ERROR")
        return ResponseEntity(body, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    companion object {
        val logger = LoggerFactory.getLogger(ProductSearchExceptionHandler::class.java)
    }
}