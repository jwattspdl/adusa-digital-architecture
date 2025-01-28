package com.peapod.dev.guidance.service_logging

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ServiceLoggingApplication

fun main(args: Array<String>) {
	runApplication<ServiceLoggingApplication>(*args)
}
