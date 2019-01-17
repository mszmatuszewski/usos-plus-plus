package pl.edu.uj.ii.mmatuszewski.services.calendar.service

import biweekly.component.VEvent
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import pl.edu.uj.ii.mmatuszewski.services.calendar.repository.RemovedEventsRepository
import java.util.concurrent.ConcurrentLinkedQueue

@Service
class CalendarCleanupService(private val removedEventsRepository: RemovedEventsRepository) {

    private val eventsToRemove = ConcurrentLinkedQueue<String>()

    suspend fun enqueue(eventsInDb: List<String>, eventsInOriginalCalendar: List<VEvent>) {
        val validEvents = eventsInOriginalCalendar.map { it.uid.value }
        eventsToRemove.addAll(eventsInDb - validEvents)
    }

    @Transactional
    @Scheduled(fixedRate = 24 * 60 * 60 * 1000, initialDelay = 10 * 60 * 1000)
    fun cleanup() {
        while (eventsToRemove.isNotEmpty()) {
            removedEventsRepository.deleteById(eventsToRemove.poll())
        }
    }
}
