package pl.edu.uj.ii.mmatuszewski.calendar.service

import biweekly.Biweekly
import biweekly.ICalendar
import biweekly.component.VEvent
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject
import pl.edu.uj.ii.mmatuszewski.auth.repository.UserRepository
import pl.edu.uj.ii.mmatuszewski.calendar.model.EventDisplay
import pl.edu.uj.ii.mmatuszewski.calendar.model.RemovedEvent
import pl.edu.uj.ii.mmatuszewski.calendar.model.toDisplay
import pl.edu.uj.ii.mmatuszewski.calendar.repository.RemovedEventsRepository

@Service
class CalendarService(val removedEventsRepository: RemovedEventsRepository, val userRepository: UserRepository) {

    fun retrieveAndFilter(owner: String): String {
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

    private fun retrieveAll(owner: String): List<VEvent> {
        val calendarUrl = userRepository.findByName(owner)?.calendarUrl ?: return emptyList()
        val calendarString: String? = RestTemplate().getForObject(calendarUrl)
        return Biweekly.parse(calendarString).first().events
    }

    @Transactional
    fun update(owner: String, events: List<String>) {
        removedEventsRepository.deleteAllByOwner(owner)
        removedEventsRepository.saveAll(events.map { RemovedEvent(it, owner) })
    }

    @Transactional
    fun setUrl(owner: String, url: String) {
        val user = userRepository.findByName(owner) ?: throw IllegalArgumentException()
        user.calendarUrl = if (url.startsWith("webcal")) url.replaceFirst("webcal", "http") else url
        userRepository.save(user)
    }

    fun getUrl(owner: String): String = userRepository.findByName(owner)?.calendarUrl ?: ""
}
