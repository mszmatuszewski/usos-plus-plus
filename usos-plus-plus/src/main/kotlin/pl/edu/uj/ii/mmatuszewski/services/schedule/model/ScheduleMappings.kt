package pl.edu.uj.ii.mmatuszewski.services.schedule.model

import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.TemporalAdjusters.*

fun List<Subject>.mapToView(): List<SubjectView> = map(Subject::mapToView)

private fun Subject.mapToView(): SubjectView =
        SubjectView(id!!, name, type, occurences.map { it.mapToView(name, type) })

private fun Event.mapToView(name: String, type: ClassType): EventView {
    val dayOf = normalisedDateTime(dayOfWeek)

    val begin = dayOf.offsetByLocalTime(start)
    val end = dayOf.offsetByLocalTime(end)

    return EventView(id!!, "${abbreviate(name)} $displayTitle $type", begin, end, selected)
}

private fun abbreviate(value: String) = value.split(" ").map { it[0] }.map(Char::toUpperCase).joinToString()

fun SubjectViews.toRenderedEvents() = subjects
        .flatMap { it.occurences }
        .filter { it.selected }
        .map(EventView::toRenderedEvent)

private fun EventView.toRenderedEvent() = RenderedEvent(id, title, start, end)

fun List<Subject>.mapToEdit(): SubjectWithEventCollection {
    val result = mutableListOf<SubjectWithEvent>()
    for (subject in this) {
        for (event in subject.occurences) {
            result += SubjectWithEvent(event.id!!, subject.name, subject.type, event.dayOfWeek, event.start, event.end,
                    event.location, event.lecturer)
        }
    }

    return SubjectWithEventCollection(result)
}

private fun LocalDateTime.offsetByLocalTime(offset: LocalTime): LocalDateTime =
        withHour(offset.hour).withMinute(offset.minute)

private fun normalisedDateTime(dayOfWeek: DayOfWeek): LocalDateTime =
        LocalDateTime.of(2018, 12, 3, 0, 0).with(nextOrSame(dayOfWeek))
