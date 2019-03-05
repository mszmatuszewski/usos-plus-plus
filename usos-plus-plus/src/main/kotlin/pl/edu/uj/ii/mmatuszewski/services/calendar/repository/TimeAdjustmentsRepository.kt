package pl.edu.uj.ii.mmatuszewski.services.calendar.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import pl.edu.uj.ii.mmatuszewski.services.calendar.model.TimeAdjustment

@Repository
interface TimeAdjustmentsRepository : CrudRepository<TimeAdjustment, String> {

    fun findByOwnerAndSummary(owner: String, summary: String): TimeAdjustment?
}
