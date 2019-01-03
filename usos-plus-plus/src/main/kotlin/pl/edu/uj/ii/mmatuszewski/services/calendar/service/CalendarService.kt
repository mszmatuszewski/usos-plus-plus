package pl.edu.uj.ii.mmatuszewski.services.calendar.service

import biweekly.Biweekly
import biweekly.ICalendar
import biweekly.component.VEvent
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import pl.edu.uj.ii.mmatuszewski.core.auth.repository.UserRepository
import pl.edu.uj.ii.mmatuszewski.services.calendar.model.EventDisplay
import pl.edu.uj.ii.mmatuszewski.services.calendar.model.RemovedEvent
import pl.edu.uj.ii.mmatuszewski.services.calendar.model.toDisplay
import pl.edu.uj.ii.mmatuszewski.services.calendar.repository.RemovedEventsRepository

@Service
class CalendarService(private val removedEventsRepository: RemovedEventsRepository,
                      private val userRepository: UserRepository,
                      private val calendarDataProvider: CalendarDataProvider) {

    fun retrieveAndFilter(usosUserId: String): String {
        val owner = userRepository.findByUsosUserId(usosUserId)?.username
                    ?: throw UsernameNotFoundException("No user with ID $usosUserId")
        val ical = ICalendar()
        val eventsToRemove = removedEventsRepository.findAllByOwner(owner).map { it.id }
        retrieveAll(owner)
                .filter { it.uid.value !in eventsToRemove }
                .forEach(ical::addEvent)
        return Biweekly.write(ical).go()
    }

    fun retrieveAllAsDisplay(owner: String): List<EventDisplay> {
        val filteredEvents = removedEventsRepository.findAllByOwner(owner).map { it.id }
        return retrieveAll(owner).map(VEvent::toDisplay).onEach { it.removed = it.id in filteredEvents }
    }

    private fun retrieveAll(owner: String): List<VEvent> = calendarDataProvider.provide(owner).events

    fun getIdFromUsername(username: String): String = userRepository.findByName(username)?.usosUserId!!

    @Transactional
    fun update(owner: String, events: List<String>) {
        removedEventsRepository.deleteAllByOwner(owner)
        removedEventsRepository.saveAll(events.map { RemovedEvent(it, owner) })
    }
}
