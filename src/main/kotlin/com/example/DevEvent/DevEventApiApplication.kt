package com.example.DevEvent

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class DevEventApiApplication

fun main(args: Array<String>) {
	runApplication<DevEventApiApplication>(*args)
}
