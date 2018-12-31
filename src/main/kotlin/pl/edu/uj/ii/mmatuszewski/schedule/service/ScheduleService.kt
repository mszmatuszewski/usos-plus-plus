package pl.edu.uj.ii.mmatuszewski.schedule.service

import org.springframework.stereotype.Service
import pl.edu.uj.ii.mmatuszewski.schedule.model.ClassType.*
import pl.edu.uj.ii.mmatuszewski.schedule.model.Event
import pl.edu.uj.ii.mmatuszewski.schedule.model.RenderedEvent
import pl.edu.uj.ii.mmatuszewski.schedule.model.Subject
import pl.edu.uj.ii.mmatuszewski.schedule.model.SubjectViews
import pl.edu.uj.ii.mmatuszewski.schedule.model.SubjectWithEvent
import pl.edu.uj.ii.mmatuszewski.schedule.model.SubjectWithEventCollection
import pl.edu.uj.ii.mmatuszewski.schedule.model.mapToEdit
import pl.edu.uj.ii.mmatuszewski.schedule.model.mapToView
import pl.edu.uj.ii.mmatuszewski.schedule.model.toRenderedEvents
import pl.edu.uj.ii.mmatuszewski.schedule.repository.ScheduleRepository
import java.time.DayOfWeek.*
import java.time.LocalTime.*
import javax.annotation.PostConstruct

@Service
class ScheduleService(val repository: ScheduleRepository) {

    fun updateSelection(owner: String, occurences: List<Long>) {
        val subjects = repository.findAllByOwner(owner)
        subjects.flatMap { it.occurences }.forEach { it.selected = it.id in occurences }
        repository.saveAll(subjects)
    }

    fun provideDisplay(owner: String): Pair<SubjectViews, List<RenderedEvent>> {
        val subjects = repository.findAllByOwner(owner)
        val subjectViews = provideSubjectViews(subjects)
        val renderedEvents = provideRenderedEvents(subjectViews)
        return Pair(subjectViews, renderedEvents)
    }

    private fun provideSubjectViews(subjects: List<Subject>): SubjectViews = SubjectViews(subjects.mapToView())

    private fun provideRenderedEvents(subjects: SubjectViews): List<RenderedEvent> = subjects.toRenderedEvents()

    fun retrieveSubjectsWithEvents(owner: String): SubjectWithEventCollection {
        return repository.findAllByOwner(owner).mapToEdit()
    }

    fun findSubjectWithEventById(owner: String, eventId: Long): SubjectWithEvent? {
        var (subject, event) = findSubjectAndEventByEventId(owner, eventId)
        if (subject == null) subject = Subject()
        if (event == null) event = Event()
        return SubjectWithEvent(event.id, subject.name, subject.type, event.dayOfWeek, event.start, event.end,
                event.location, event.lecturer)
    }

    fun addOrUpdateSubjectWithEvent(owner: String, subjectWithEvent: SubjectWithEvent) {
        val (subject, event) = findSubjectAndEventByEventId(owner, subjectWithEvent.id)
        val updated = when {
            subject == null -> update(owner, subjectWithEvent)
            event == null   -> update(subject, subjectWithEvent)
            else            -> update(subject, event, subjectWithEvent)
        }
        repository.save(updated)
    }

    private fun update(owner: String, subjectWithEvent: SubjectWithEvent): Subject = with(subjectWithEvent) {
        val event = Event(
                dayOfWeek = dayOfWeek,
                start = start,
                end = end,
                location = location,
                lecturer = lecturer,
                selected = true)
        Subject(name = name, owner = owner, type = type, occurences = mutableListOf(event))
    }

    private fun update(subject: Subject, subjectWithEvent: SubjectWithEvent): Subject =
            with(subjectWithEvent) {
                val event =
                        Event(dayOfWeek = dayOfWeek, start = start, end = end, location = location, lecturer = lecturer)
                subject.occurences += event
                return subject
            }

    private fun update(subject: Subject, event: Event, subjectWithEvent: SubjectWithEvent): Subject =
            with(subjectWithEvent) {
                subject.name = name
                subject.type = type
                event.dayOfWeek = dayOfWeek
                event.start = start
                event.end = end
                event.location = location
                event.lecturer = lecturer
                return subject
            }

    fun deleteSubjectWithEvent(owner: String, eventId: Long) {
        val (subject, event) = findSubjectAndEventByEventId(owner, eventId)
        if (subject == null || event == null) throw NoSuchElementException()
        subject.occurences.remove(event)
        when {
            subject.occurences.isNotEmpty() -> {
                if (event.selected) {
                    subject.occurences[0].selected = true
                }
                repository.save(subject)
            }
            else                            -> repository.delete(subject)
        }
    }

    private fun findSubjectAndEventByEventId(owner: String, eventId: Long?): Pair<Subject?, Event?> {
        val subjects = repository.findAllByOwner(owner)
        val subject = subjects.find { it.occurences.any { it.id == eventId } }
        val event = subject?.occurences?.findLast { it.id == eventId }
        return Pair(subject, event)
    }

    @PostConstruct
    fun init() {
        //TODO: remove before deploy

        repository.saveAll(listOf(
                Subject(owner = "user", name = "Effective Python", type = LECTURE,
                        occurences =
                        mutableListOf(Event(dayOfWeek = WEDNESDAY,
                                start = of(16, 0),
                                end = of(17, 30),
                                location = "1177",
                                lecturer = "dr Krzysztof Misztal",
                                selected = true))),
                Subject(owner = "user", name = "Assembly programming", type = CLASS,
                        occurences =
                        mutableListOf(Event(dayOfWeek = WEDNESDAY,
                                start = of(9, 45),
                                end = of(12, 0),
                                location = "0056",
                                lecturer =
                                "dr Tomasz Kapela", selected = true),
                                Event(dayOfWeek = WEDNESDAY, start = of(12, 15), end = of(14, 30), location = "0056",
                                        lecturer = "dr Tomasz Kapela"))),
                Subject(owner = "user", name = "Advanced design and architectural patterns", type = CLASS,
                        occurences =
                        mutableListOf(Event(dayOfWeek = TUESDAY,
                                start = of(8, 0),
                                end = of(9, 30),
                                location = "0059",
                                lecturer =
                                "dr Marcin Żelawski", selected = true),
                                Event(dayOfWeek = THURSDAY, start = of(15, 45), end = of(17, 15), location = "0056",
                                        lecturer =
                                        "dr Marcin Żelawski"))),
                Subject(owner = "user", name = "Abstract programming", type = CLASS,
                        occurences =
                        mutableListOf(Event(dayOfWeek = THURSDAY, start = of(8, 30), end = of(10, 0), location = "0053",
                                lecturer =
                                "dr Sylwester Arabas", selected = true),
                                Event(dayOfWeek = THURSDAY, start = of(18, 0), end = of(19, 30), location = "0053",
                                        lecturer = "mgr Maciej Szymczak"))),
                Subject(owner = "user", name = "Effective Python", type = CLASS,
                        occurences =
                        mutableListOf(Event(dayOfWeek = WEDNESDAY,
                                start = of(17, 45),
                                end = of(19, 15),
                                location = "0053",
                                lecturer = "mgr Maciej Szymczak"),
                                Event(dayOfWeek = WEDNESDAY, start = of(17, 45), end = of(19, 15), location = "0056",
                                        lecturer =
                                        "dr Paweł Bogdan", selected = true),
                                Event(dayOfWeek = WEDNESDAY,
                                        start = of(17, 45),
                                        end = of(19, 15),
                                        location = "0059",
                                        lecturer = "mgr Jacek Kubica")))
        ))
    }
}
