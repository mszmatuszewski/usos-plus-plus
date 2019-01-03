package pl.edu.uj.ii.mmatuszewski.services.grades.service

import com.github.scribejava.core.oauth.OAuth10aService
import org.springframework.stereotype.Service
import pl.edu.uj.ii.mmatuszewski.core.auth.model.User
import pl.edu.uj.ii.mmatuszewski.core.auth.service.LocalUserService
import pl.edu.uj.ii.mmatuszewski.core.usos.service.AbstractUsosConnectorService
import pl.edu.uj.ii.mmatuszewski.services.grades.model.CourseDetails
import pl.edu.uj.ii.mmatuszewski.services.grades.model.CourseLocator

@Service
class CourseDetailsDataProvider(usosConnector: OAuth10aService, localUserService: LocalUserService) :
        AbstractUsosConnectorService<CourseDetails>(usosConnector, localUserService) {

    override val serviceUrlPath: String = "services/courses/course"

    fun provide(owner: User, locator: CourseLocator): CourseDetails =
            sendAndReceive(owner, mapOf("course_id" to locator.courseId!!, "fields" to "id|ects_credits_simplified"))

    override fun extractResponse(responseBody: String): CourseDetails =
            mapper.readValue(responseBody, CourseDetails::class.java)
}
