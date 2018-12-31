package pl.edu.uj.ii.mmatuszewski.calendar.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import pl.edu.uj.ii.mmatuszewski.calendar.model.RemovedEvent

@Repository
interface RemovedEventsRepository : CrudRepository<RemovedEvent, Long> {

    fun findAllByOwner(owner: String): List<RemovedEvent>

    fun deleteAllByOwner(owner: String)
}
