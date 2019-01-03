package pl.edu.uj.ii.mmatuszewski.services.calendar.service

import biweekly.Biweekly
import biweekly.ICalendar
import com.github.scribejava.core.oauth.OAuth10aService
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException
import org.springframework.stereotype.Service
import pl.edu.uj.ii.mmatuszewski.core.auth.model.User
import pl.edu.uj.ii.mmatuszewski.core.auth.service.LocalUserService
import pl.edu.uj.ii.mmatuszewski.core.usos.service.AbstractUsosConnectorService

@Service
class CalendarDataProvider(usosConnector: OAuth10aService,
                           localUserService: LocalUserService) :
        AbstractUsosConnectorService<ICalendar>(usosConnector, localUserService) {

    override val serviceUrlPath: String = "services/tt/upcoming_ical"

    fun provide(owner: String): ICalendar {
        val user = localUserService.loadUserByUsername(owner) as User
        val userId = user.usosUserId ?: throw PreAuthenticatedCredentialsNotFoundException("User not signed in")
        return sendAndReceive(user, mapOf("user_id" to userId, "lang" to "pl"))
    }

    override fun extractResponse(responseBody: String): ICalendar = Biweekly.parse(responseBody).first()
}
