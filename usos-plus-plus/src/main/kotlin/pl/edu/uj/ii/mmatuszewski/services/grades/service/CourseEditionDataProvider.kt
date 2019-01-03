package pl.edu.uj.ii.mmatuszewski.services.grades.service

import com.github.scribejava.core.oauth.OAuth10aService
import org.springframework.stereotype.Service
import pl.edu.uj.ii.mmatuszewski.core.auth.model.User
import pl.edu.uj.ii.mmatuszewski.core.auth.service.LocalUserService
import pl.edu.uj.ii.mmatuszewski.core.usos.service.AbstractUsosConnectorService
import pl.edu.uj.ii.mmatuszewski.services.grades.model.CourseEdition
import pl.edu.uj.ii.mmatuszewski.services.grades.model.CourseLocator

@Service
class CourseEditionDataProvider(usosConnector: OAuth10aService, localUserService: LocalUserService) :
        AbstractUsosConnectorService<CourseEdition>(usosConnector, localUserService) {

    override val serviceUrlPath: String = "services/courses/course_edition"

    fun provide(owner: User, locator: CourseLocator): CourseEdition = sendAndReceive(owner, mapOf(
            "course_id" to locator.courseId!!,
            "term_id" to locator.termId!!,
            "fields" to "course_id|course_name|term_id|grades"))

    override fun extractResponse(responseBody: String): CourseEdition =
            mapper.readValue(responseBody, CourseEdition::class.java)
}
