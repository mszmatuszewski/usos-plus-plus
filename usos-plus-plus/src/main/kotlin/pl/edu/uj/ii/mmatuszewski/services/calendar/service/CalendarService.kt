package pl.edu.uj.ii.mmatuszewski.services.calendar.service

import biweekly.Biweekly
import biweekly.ICalendar
import biweekly.component.VEvent
import org.slf4j.LoggerFactory
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

    private val LOGGER = LoggerFactory.getLogger(CalendarService::class.java)

    fun retrieveAndFilter(usosUserId: String): String {
        val owner = userRepository.findByUsosUserId(usosUserId)?.username
                    ?: throw UsernameNotFoundException("No user with ID $usosUserId")
        LOGGER.info("Calendar update requested for user $owner with ID $usosUserId")
        val eventsToRemove = removedEventsRepository.findAllByOwner(owner).map { it.id }
        val originalCalendar = retrieveAll(owner)
        val newCalendar = ICalendar()
        newCalendar.timezoneInfo = originalCalendar.timezoneInfo
        newCalendar.productId = originalCalendar.productId
        newCalendar.refreshInterval = originalCalendar.refreshInterval
        originalCalendar.experimentalProperties.forEach { newCalendar.setExperimentalProperty(it.name, it.dataType, it.value) }
        originalCalendar.events.filter { it.uid.value !in eventsToRemove }.forEach(newCalendar::addEvent)
        return Biweekly.write(newCalendar).go()
    }

    fun retrieveAllAsDisplay(owner: String): List<EventDisplay> {
        val filteredEvents = removedEventsRepository.findAllByOwner(owner).map { it.id }
        return retrieveAll(owner).events.map(VEvent::toDisplay).onEach { it.removed = it.id in filteredEvents }
    }

    private fun retrieveAll(owner: String): ICalendar = calendarDataProvider.provide(owner)

    fun getIdFromUsername(username: String): String = userRepository.findByName(username)?.usosUserId!!

    @Transactional
    fun update(owner: String, events: List<String>) {
        removedEventsRepository.deleteAllByOwner(owner)
        removedEventsRepository.saveAll(events.map { RemovedEvent(it, owner) })
    }
}
