package pl.edu.uj.ii.mmatuszewski

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties
class SpringApplication

fun main(args: Array<String>) {
    runApplication<SpringApplication>(*args)
}

fun Double.roundToTwoDecimalPlacesAsString(): String {
    return String.format("%.2f", this)
}
