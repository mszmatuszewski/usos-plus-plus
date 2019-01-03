package pl.edu.uj.ii.mmatuszewski.core.usos

import com.github.scribejava.core.builder.ServiceBuilder
import com.github.scribejava.core.builder.api.DefaultApi10a
import com.github.scribejava.core.builder.api.OAuth1SignatureType
import com.github.scribejava.core.model.Verb
import com.github.scribejava.core.oauth.OAuth10aService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.edu.uj.ii.mmatuszewski.core.usos.model.UsosAuthenticationDetailsProvider

@Configuration
class OAuthConfig(private val usosAuthenticationDetailsProvider: UsosAuthenticationDetailsProvider) {

    @Bean
    fun usosConnectorService(): OAuth10aService =
            ServiceBuilder(usosAuthenticationDetailsProvider.key)
                    .apiSecret(usosAuthenticationDetailsProvider.secret)
                    .callback("${usosAuthenticationDetailsProvider.appUrl}/oauth/callback")
                    .scope(usosAuthenticationDetailsProvider.scopes)
                    .build(usosApi())

    @Bean
    fun usosApi(): DefaultApi10a = object : DefaultApi10a() {

        override fun getRequestTokenVerb(): Verb = Verb.GET

        override fun getSignatureType(): OAuth1SignatureType = OAuth1SignatureType.QueryString

        override fun getRequestTokenEndpoint(): String = usosAuthenticationDetailsProvider.requestTokenUrl

        override fun getAuthorizationBaseUrl(): String = usosAuthenticationDetailsProvider.userAuthUrl

        override fun getAccessTokenEndpoint(): String = usosAuthenticationDetailsProvider.accessTokenUrl
    }
}
