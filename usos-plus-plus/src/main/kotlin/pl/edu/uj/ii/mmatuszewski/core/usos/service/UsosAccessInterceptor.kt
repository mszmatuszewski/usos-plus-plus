package pl.edu.uj.ii.mmatuszewski.core.usos.service

import com.github.scribejava.core.oauth.OAuth10aService
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException
import org.springframework.stereotype.Service
import org.springframework.web.servlet.HandlerInterceptor
import pl.edu.uj.ii.mmatuszewski.core.auth.model.RequestTokenWrapper
import pl.edu.uj.ii.mmatuszewski.core.auth.model.User
import pl.edu.uj.ii.mmatuszewski.core.auth.service.LocalUserService
import pl.edu.uj.ii.mmatuszewski.core.usos.repository.RequestTokenWrapperRepository
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Service
class UsosAccessInterceptor(private val localUserService: LocalUserService,
                            private val usosConnector: OAuth10aService,
                            private val usosUserDataProvider: UsosUserDataProvider,
                            private val requestTokenWrapperRepository: RequestTokenWrapperRepository
) : HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val user = localUserService.loadUserByUsername(request.userPrincipal.name) as User
        if (validAccessToken(user)) return true

        val requestToken = usosConnector.requestToken
        response.sendRedirect(usosConnector.getAuthorizationUrl(requestToken))

        val requestTokenWrapper =
                RequestTokenWrapper(user.username, requestToken.token, requestToken.tokenSecret, request.contextPath)
        requestTokenWrapperRepository.save(requestTokenWrapper)
        return false
    }

    private fun validAccessToken(user: User): Boolean {
        return try {
            usosUserDataProvider.provide(user)
            true
        } catch (e: PreAuthenticatedCredentialsNotFoundException) {
            false
        }
    }
}
