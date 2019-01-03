package pl.edu.uj.ii.mmatuszewski.core.usos.service

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.scribejava.core.model.OAuth1AccessToken
import com.github.scribejava.core.model.OAuthRequest
import com.github.scribejava.core.model.Verb
import com.github.scribejava.core.oauth.OAuth10aService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException
import org.springframework.web.client.HttpClientErrorException
import pl.edu.uj.ii.mmatuszewski.core.auth.model.User
import pl.edu.uj.ii.mmatuszewski.core.auth.service.LocalUserService

abstract class AbstractUsosConnectorService<RS>(
        protected val usosConnector: OAuth10aService,
        protected val localUserService: LocalUserService
) {

    private val LOGGER = LoggerFactory.getLogger(this.javaClass)
    private val INVALID_ACCESS_TOKEN = "Invalid access token"
    protected val mapper: ObjectMapper =
            ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    @Value("\${usos.api.baseurl}")
    private lateinit var baseUrl: String

    protected abstract val serviceUrlPath: String

    private val endpoint
        get() = baseUrl + serviceUrlPath

    protected fun sendAndReceive(resourceOwner: User): RS = sendAndReceive(resourceOwner, emptyMap())

    protected fun sendAndReceive(resourceOwner: User, params: Map<String, String>): RS {
        val request = encodeRequest(endpoint, params)
        val accessToken = getAccessToken(resourceOwner)
        usosConnector.signRequest(accessToken, request)
        val response = usosConnector.execute(request)
        if (response.code !in 200..299) {
            LOGGER.error("Invalid response {}}", response.body)
            if (response.code == 401) {
                invalidateAccessToken(resourceOwner)
                throw PreAuthenticatedCredentialsNotFoundException(INVALID_ACCESS_TOKEN)
            }
            throw HttpClientErrorException(HttpStatus.valueOf(response.code))
        }
        return extractResponse(response.body)
    }

    private fun invalidateAccessToken(resourceOwner: User) {
        localUserService.invalidateAccessToken(resourceOwner)
    }

    private fun encodeRequest(endpoint: String, params: Map<String, String>): OAuthRequest =
            OAuthRequest(Verb.GET, endpoint).apply {
                params.forEach { addQuerystringParameter(it.key, it.value) }
            }

    protected abstract fun extractResponse(responseBody: String): RS

    protected fun getAccessToken(user: User): OAuth1AccessToken {
        val accessToken = user.usosAccessToken
                          ?: throw PreAuthenticatedCredentialsNotFoundException("User not signed in")
        val accessTokenSecret = user.usosAccessSecret
                                ?: throw PreAuthenticatedCredentialsNotFoundException("User not signed in")
        return OAuth1AccessToken(accessToken, accessTokenSecret)
    }
}
