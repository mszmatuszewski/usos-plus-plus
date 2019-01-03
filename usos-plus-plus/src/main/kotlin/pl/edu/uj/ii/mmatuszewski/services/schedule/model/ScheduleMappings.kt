package pl.edu.uj.ii.mmatuszewski.services.schedule.model

import java.time.DayOfWeek
import java.time.DayOfWeek.*
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.TemporalAdjusters.*

fun List<Subject>.mapToView(): List<SubjectView> = map(Subject::mapToView)

private fun Subject.mapToView(): SubjectView = SubjectView(id!!, name, type, occurences.map(Event::mapToView))

private fun Event.mapToView(): EventView {
    val dayOf = LocalDateTime.now().normalised(dayOfWeek)

    val begin = dayOf.offsetByLocalTime(start)
    val end = dayOf.offsetByLocalTime(end)

    return EventView(id!!, displayTitle, begin, end, selected)
}

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

private fun LocalDateTime.normalised(dayOfWeek: DayOfWeek): LocalDateTime =
        with(previousOrSame(MONDAY)).with(nextOrSame(dayOfWeek))
