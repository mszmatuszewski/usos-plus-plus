package pl.edu.uj.ii.mmatuszewski.services.calendar.service

import biweekly.component.VEvent
import biweekly.property.DateEnd
import biweekly.property.DateStart
import org.springframework.stereotype.Service
import pl.edu.uj.ii.mmatuszewski.services.calendar.model.TimeAdjustment
import pl.edu.uj.ii.mmatuszewski.services.calendar.model.toLocalDateTime
import pl.edu.uj.ii.mmatuszewski.services.calendar.model.toPolishDate
import pl.edu.uj.ii.mmatuszewski.services.calendar.repository.TimeAdjustmentsRepository

@Service
class TimeAdjustmentService(private val timeAdjustmentsRepository: TimeAdjustmentsRepository) {

    fun adjust(event: VEvent, owner: String): VEvent {
        timeAdjustmentsRepository.findByOwnerAndSummary(owner, event.summary.value)?.let {
            adjust(event, it)
        }
        return event
    }

    private fun adjust(event: VEvent, adjustment: TimeAdjustment): VEvent {
        val (_, _, _, startOffset, durationOffset) = adjustment
        val adjustedStart = event.dateStart.toLocalDateTime().plusMinutes(startOffset)
        val adjustedEnd = event.dateEnd.toLocalDateTime().plusMinutes(startOffset).plusMinutes(durationOffset)
        event.dateStart = DateStart(adjustedStart.toPolishDate())
        event.dateEnd = DateEnd(adjustedEnd.toPolishDate())
        return event
    }
}
