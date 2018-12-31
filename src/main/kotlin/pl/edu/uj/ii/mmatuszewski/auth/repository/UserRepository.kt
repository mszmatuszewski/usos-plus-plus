package pl.edu.uj.ii.mmatuszewski.auth.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import pl.edu.uj.ii.mmatuszewski.auth.model.User

@Repository
interface UserRepository : CrudRepository<User, Long> {

    fun findByName(username: String): User?
}
