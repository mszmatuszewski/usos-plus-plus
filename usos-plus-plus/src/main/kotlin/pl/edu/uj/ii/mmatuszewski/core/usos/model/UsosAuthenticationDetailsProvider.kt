package pl.edu.uj.ii.mmatuszewski.core.usos.model

import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

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

    @PostConstruct
    fun logDetails() {
        LOGGER.info("Initialized UsosAuthenticationDetailsProvider")
        LOGGER.info("usos.consumer.key=${key}")
        LOGGER.info("usos.consumer.secret=${secret}")
        LOGGER.info("usos.consumer.requestTokenUrl=${requestTokenUrl}")
        LOGGER.info("usos.consumer.userAuthUrl=${userAuthUrl}")
        LOGGER.info("usos.consumer.accessTokenUrl=${accessTokenUrl}")
        LOGGER.info("usos.consumer.appUrl=${appUrl}")
        LOGGER.info("usos.consumer.scopes=${scopes}")
    }
}
