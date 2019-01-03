package pl.edu.uj.ii.mmatuszewski.core.usos.service

import com.github.scribejava.core.oauth.OAuth10aService
import org.springframework.stereotype.Service
import pl.edu.uj.ii.mmatuszewski.core.auth.model.User
import pl.edu.uj.ii.mmatuszewski.core.auth.service.LocalUserService
import pl.edu.uj.ii.mmatuszewski.core.usos.model.UsosUserDetails

@Service
class UsosUserDataProvider(usosConnector: OAuth10aService, localUserService: LocalUserService) :
        AbstractUsosConnectorService<UsosUserDetails>(usosConnector, localUserService) {

    override val serviceUrlPath: String = "services/users/user"

    fun provide(user: User): UsosUserDetails = sendAndReceive(user, mapOf("fields" to "id|first_name|last_name|email"))

    override fun extractResponse(responseBody: String): UsosUserDetails =
            mapper.readValue(responseBody, UsosUserDetails::class.java)
}
