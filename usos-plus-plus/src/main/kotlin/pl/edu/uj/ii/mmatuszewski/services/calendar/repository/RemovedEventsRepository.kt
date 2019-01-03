package pl.edu.uj.ii.mmatuszewski.services.calendar.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import pl.edu.uj.ii.mmatuszewski.services.calendar.model.RemovedEvent

@Repository
interface RemovedEventsRepository : CrudRepository<RemovedEvent, Long> {

    fun findAllByOwner(owner: String): List<RemovedEvent>

    fun deleteAllByOwner(owner: String)
}
