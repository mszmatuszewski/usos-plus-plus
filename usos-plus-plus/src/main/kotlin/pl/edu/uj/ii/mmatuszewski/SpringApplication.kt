package pl.edu.uj.ii.mmatuszewski

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity

@SpringBootApplication
@EnableConfigurationProperties
@EnableScheduling
@EnableCaching
@EnableWebSecurity(debug = true)
class SpringApplication

fun main(args: Array<String>) {
    runApplication<SpringApplication>(*args)
}

