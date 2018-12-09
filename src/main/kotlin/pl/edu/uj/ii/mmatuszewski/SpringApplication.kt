package pl.edu.uj.ii.mmatuszewski

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import java.math.BigDecimal
import java.math.RoundingMode

@SpringBootApplication
@EnableConfigurationProperties
class SpringApplication

fun main(args: Array<String>) {
    runApplication<SpringApplication>(*args)
}

fun Double.roundToTwoDecimalPlaces(): Double {
    val bd = BigDecimal(this)
    bd.setScale(2, RoundingMode.HALF_UP)
    return bd.toDouble()
}
