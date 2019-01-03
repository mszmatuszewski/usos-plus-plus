package pl.edu.uj.ii.mmatuszewski.core.usos.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import pl.edu.uj.ii.mmatuszewski.core.auth.model.RequestTokenWrapper

@Repository
interface RequestTokenWrapperRepository : CrudRepository<RequestTokenWrapper, String> {

    fun findByRequestToken(requestToken: String): RequestTokenWrapper
}
