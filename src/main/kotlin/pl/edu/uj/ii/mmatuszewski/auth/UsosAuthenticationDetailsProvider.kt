package pl.edu.uj.ii.mmatuszewski.auth

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "usos.consumer")
class UsosAuthenticationDetailsProvider {

    lateinit var key: String
    lateinit var secret: String

    lateinit var requestTokenUrl: String
    lateinit var userAuthUrl: String
    lateinit var accessTokenUrl: String
}
