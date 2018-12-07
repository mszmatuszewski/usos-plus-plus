package pl.edu.uj.ii.mmatuszewski.auth

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth.common.signature.SharedConsumerSecretImpl
import org.springframework.security.oauth.consumer.BaseProtectedResourceDetails
import org.springframework.security.oauth.consumer.InMemoryProtectedResourceDetailsService
import org.springframework.security.oauth.consumer.ProtectedResourceDetails
import org.springframework.security.oauth.consumer.ProtectedResourceDetailsService
import org.springframework.security.oauth.consumer.client.OAuthRestTemplate

@Configuration
class OAuthConfig(val detailsProvider: UsosAuthenticationDetailsProvider) {

    @Bean
    fun usosResource(): ProtectedResourceDetails {
        val details = BaseProtectedResourceDetails()

        details.id = "usosResource"
        details.consumerKey = detailsProvider.key
        details.sharedSecret = SharedConsumerSecretImpl(detailsProvider.secret)
        details.requestTokenURL = detailsProvider.requestTokenUrl
        details.userAuthorizationURL = detailsProvider.userAuthUrl
        details.accessTokenURL = detailsProvider.accessTokenUrl

        return details
    }

    @Bean
    fun detailsService(usosResourceDetails: ProtectedResourceDetails): ProtectedResourceDetailsService {
        val service = InMemoryProtectedResourceDetailsService()
        service.resourceDetailsStore = mapOf(usosResourceDetails.id to usosResourceDetails)
        return service
    }

    @Bean
    fun oauthRestTemplate(): OAuthRestTemplate {
        return OAuthRestTemplate(usosResource())
    }
}
