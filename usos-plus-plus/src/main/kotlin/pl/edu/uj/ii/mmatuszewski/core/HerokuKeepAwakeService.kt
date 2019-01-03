package pl.edu.uj.ii.mmatuszewski.core

import org.slf4j.LoggerFactory.*
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject
import pl.edu.uj.ii.mmatuszewski.core.usos.model.UsosAuthenticationDetailsProvider

@Service
class HerokuKeepAwakeService(private val usosAuthenticationDetailsProvider: UsosAuthenticationDetailsProvider) {

    private val LOGGER = getLogger(HerokuKeepAwakeService::class.java)

    @Scheduled(fixedRate = 15 * 60 * 1000)
    fun ping() {
        RestTemplate().getForObject<String>(usosAuthenticationDetailsProvider.appUrl)
        LOGGER.info("Self-pinged ${usosAuthenticationDetailsProvider.appUrl}")
    }
}
