package pl.edu.uj.ii.mmatuszewski.services.grades.service

import com.github.scribejava.core.oauth.OAuth10aService
import org.springframework.stereotype.Service
import pl.edu.uj.ii.mmatuszewski.core.auth.model.User
import pl.edu.uj.ii.mmatuszewski.core.auth.service.LocalUserService
import pl.edu.uj.ii.mmatuszewski.core.usos.service.AbstractUsosConnectorService
import pl.edu.uj.ii.mmatuszewski.services.grades.model.CoursesUserRS

@Service
class UserCoursesDataProvider(usosConnector: OAuth10aService, localUserService: LocalUserService) :
        AbstractUsosConnectorService<CoursesUserRS>(usosConnector, localUserService) {

    override val serviceUrlPath: String = "services/courses/user"

    fun provide(owner: String): CoursesUserRS {
        val user = localUserService.loadUserByUsername(owner) as User
        return sendAndReceive(user, mapOf("active_terms_only" to "false"))
    }

    override fun extractResponse(responseBody: String): CoursesUserRS =
            mapper.readValue(responseBody, CoursesUserRS::class.java)
}
