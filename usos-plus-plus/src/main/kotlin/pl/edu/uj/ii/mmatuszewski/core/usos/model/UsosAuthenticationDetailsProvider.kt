package pl.edu.uj.ii.mmatuszewski.core.usos.model

import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "usos.consumer")
class UsosAuthenticationDetailsProvider {

    private val LOGGER = LoggerFactory.getLogger(UsosAuthenticationDetailsProvider::class.java)

    lateinit var key: String
    lateinit var secret: String

    lateinit var requestTokenUrl: String
    lateinit var userAuthUrl: String
    lateinit var accessTokenUrl: String

    lateinit var appUrl: String
    lateinit var scopes: String
}
