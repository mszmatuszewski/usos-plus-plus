package pl.edu.uj.ii.mmatuszewski.services.schedule.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import pl.edu.uj.ii.mmatuszewski.services.schedule.model.Subject

@Repository
interface ScheduleRepository : CrudRepository<Subject, Int> {

    fun findAllByOwner(owner: String): List<Subject>
    fun deleteAllByOwner(owner: String)
}
