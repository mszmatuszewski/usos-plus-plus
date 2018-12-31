package pl.edu.uj.ii.mmatuszewski.schedule.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import pl.edu.uj.ii.mmatuszewski.schedule.model.Subject

@Repository
interface ScheduleRepository : CrudRepository<Subject, Int> {

    fun findAllByOwner(owner: String): List<Subject>
    fun findByName(name: String): Subject?
}
