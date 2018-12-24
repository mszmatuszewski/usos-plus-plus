package pl.edu.uj.ii.mmatuszewski.schedule.services

import org.slf4j.LoggerFactory
import org.springframework.core.env.Environment
import org.springframework.core.env.get
import org.springframework.stereotype.Service
import pl.edu.uj.ii.mmatuszewski.schedule.ClassType.*
import pl.edu.uj.ii.mmatuszewski.schedule.Event
import pl.edu.uj.ii.mmatuszewski.schedule.EventView
import pl.edu.uj.ii.mmatuszewski.schedule.Subject
import java.time.DayOfWeek.*
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.LocalTime.*
import java.time.temporal.TemporalAdjusters.*
import javax.annotation.PostConstruct

@Service
class ClassesService {

    fun provide(): List<Subject> = listOf(
            Subject(1, "Effective Python", LECTURE,
                    listOf(Event(1, WEDNESDAY, of(16, 0), of(17, 30), "1177", "dr Krzysztof Misztal"))),
            Subject(2, "Assembly programming", CLASS,
                    listOf(Event(2, WEDNESDAY, of(9, 45), of(12, 0), "0056", "dr Tomasz Kapela"),
                            Event(3, WEDNESDAY, of(12, 15), of(14, 30), "0056", "dr Tomasz Kapela"))),
            Subject(3, "Advanced design and architectural patterns", CLASS,
                    listOf(Event(4, TUESDAY, of(8, 0), of(9, 30), "0059", "dr Marcin Żelawski"),
                            Event(5, THURSDAY, of(15, 45), of(17, 15), "0056", "dr Marcin Żelawski"))),
            Subject(4, "Abstract programming", CLASS,
                    listOf(Event(6, THURSDAY, of(8, 30), of(10, 0), "0053", "dr Sylwester Arabas"),
                            Event(7, THURSDAY, of(18, 0), of(19, 30), "0053", "mgr Maciej Szymczak"))),
            Subject(5, "Effective Python", CLASS,
                    listOf(Event(8, WEDNESDAY, of(17, 45), of(19, 15), "0053", "mgr Maciej Szymczak"),
                            Event(9, WEDNESDAY, of(17, 45), of(19, 15), "0056", "dr Paweł Bogdan"),
                            Event(10, WEDNESDAY, of(17, 45), of(19, 15), "0059", "mgr Jacek Kubica")))
    )

    fun mapToView(subjects: List<Subject>): List<EventView> {
        return subjects.flatMap(::mapToView)
    }

    private fun mapToView(subject: Subject): List<EventView> =
            subject.occurences.map { mapToView(it, subject.name) }

    private fun mapToView(event: Event, subjectName: String): EventView {
        val onDay = LocalDateTime.now().normalisedToMonday().with(next(event.dayOfWeek))

        val begin = onDay.withHourlyOffset(event.start)
        val end = onDay.withHourlyOffset(event.end)

        return EventView(event.id, subjectName, begin, end, event.selected)
    }

    private fun LocalDateTime.normalisedToMonday(): LocalDateTime =
            if (this.dayOfWeek == MONDAY) this else this.with(previous(MONDAY))

    private fun LocalDateTime.withHourlyOffset(offset: LocalTime): LocalDateTime =
            this.withHour(offset.hour).withMinute(offset.minute)

    @PostConstruct
    fun logDbDetails(environment: Environment) {
        val logger = LoggerFactory.getLogger(ClassesService::class.java)
        logger.error("SPRING_DATASOURCE_URL {}", environment["spring.datasource.url"])
        logger.error("SPRING_DATASOURCE_USERNAME {}", environment["spring.datasource.username"])
        logger.error("SPRING_DATASOURCE_PASSWORD {}", environment["spring.datasource.password"])

    }
}
