package pl.edu.uj.ii.mmatuszewski.core.usos.controller

import com.github.scribejava.core.model.OAuth1RequestToken
import com.github.scribejava.core.oauth.OAuth10aService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import pl.edu.uj.ii.mmatuszewski.core.auth.model.User
import pl.edu.uj.ii.mmatuszewski.core.auth.service.LocalUserService
import pl.edu.uj.ii.mmatuszewski.core.usos.repository.RequestTokenWrapperRepository
import pl.edu.uj.ii.mmatuszewski.core.usos.service.UsosUserDataProvider
import pl.edu.uj.ii.mmatuszewski.services.grades.service.GradeRefreshService
import javax.servlet.http.HttpServletRequest

@Controller
@RequestMapping("/oauth/callback")
class OAuthCallbackController(private val usosConnector: OAuth10aService,
                              private val localUserService: LocalUserService,
                              private val userDataProvider: UsosUserDataProvider,
                              private val requestTokenWrapperRepository: RequestTokenWrapperRepository,
                              private val gradeRefreshService: GradeRefreshService) {

    @GetMapping
    @Transactional
    fun authenticate(request: HttpServletRequest,
                     @RequestParam("oauth_token") token: String,
                     @RequestParam("oauth_verifier") verifier: String): String {
        val requestTokenWrapper = requestTokenWrapperRepository.findByRequestToken(token)
        val requestToken = OAuth1RequestToken(requestTokenWrapper.requestToken, requestTokenWrapper.requestTokenSecret)

        val accessToken = usosConnector.getAccessToken(requestToken, verifier)

        val user = localUserService.loadUserByUsername(requestTokenWrapper.owner) as User
        localUserService.saveAccessToken(user, accessToken.token, accessToken.tokenSecret)
        requestTokenWrapperRepository.delete(requestTokenWrapper)

        localUserService.saveUsosId(user, userDataProvider.provide(user).id!!)

        GlobalScope.launch { gradeRefreshService.refresh() }

        val redirection = requestTokenWrapper.redirection
        return "redirect:" + if (redirection.isBlank()) "/" else redirection
    }
}
